package com.github.pozo.bkkinfo.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.pozo.bkkinfo.activities.BasicPreferenceActivity;
import com.github.pozo.bkkinfo.model.Entry.EntryParser;
import com.github.pozo.bkkinfo.utils.Constants;

public class Model {
	public static class ModelParser {
		public static Model parse(JSONObject jsonObject) throws JSONException {
			Model retval = new Model();
			
			for (Type type : Type.values()) {
				parseList(type, jsonObject, retval);
			}
			return retval;
		}

		private static void parseList(Type type, JSONObject jsonObject, Model retval) throws JSONException  {
			JSONArray objects = jsonObject.getJSONArray(type.name().toLowerCase(Locale.ENGLISH));

			for (int i = 0; i < objects.length(); i++) {
				JSONObject oneObject = objects.getJSONObject(i);
				retval.addEntry(type, EntryParser.parse(oneObject));
			}
		}
	}
	
	private static final String API_URL = "http://www.bkk.hu/apps/bkkinfo/lista-api.php";
	private static final String EMPTY_RESULT = "{\"active\":[],\"soon\":[],\"future\":[]}";
	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	
	private static Model model = null;
	
	public enum Type {
		ACTIVE,SOON,FUTURE;
	};
	
	private HashMap<Type, ArrayList<Entry>> entries = initializeEntries();

	private static HashMap<Type, ArrayList<Entry>> initializeEntries() {
		HashMap<Type, ArrayList<Entry>> entries = new HashMap<Type, ArrayList<Entry>>();
		
		entries.put(Type.ACTIVE, new ArrayList<Entry>());
		entries.put(Type.SOON, new ArrayList<Entry>());
		entries.put(Type.FUTURE, new ArrayList<Entry>());
		
		return entries;
	}
	
	private static boolean isExists() {
		return model != null;
	}
	private static boolean isEmpty() {
		if(isExists()) {
			return model.getAllEntry().isEmpty();
		}
		return true;
	}
	public static boolean isNeedUpdate() {
		return !isExists() || isEmpty();
	}
	public static synchronized void updateModel(Context context) {
		try {
			JSONObject jObject = new JSONObject(getJSON(context));
			model = ModelParser.parse(jObject);
		} catch (JSONException e) {
			Log.e(Constants.LOG_TAG, e.getMessage());
			e.printStackTrace();
		}
	}
	public static synchronized Model getModel(Context context) {
		if(isNeedUpdate()) {
			updateModel(context);
		}
		return model;
	}

	public ArrayList<Entry> getAllEntry() {
		ArrayList<Entry> allEntry = new ArrayList<Entry>();

		allEntry.addAll(entries.get(Type.ACTIVE));
		allEntry.addAll(entries.get(Type.SOON));
		allEntry.addAll(entries.get(Type.FUTURE));
		
		return allEntry;
	}

	public ArrayList<Entry> getFilteredList(Type type, ArrayList<String> filteredLines) {
		if(filteredLines.isEmpty()) {
			return entries.get(type);
		} else {
			ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
			
			for (Entry entry : entries.get(type)) {
				if(entry.hasAtLeastOneLineType(filteredLines)) {
					filteredEntries.add(entry);
				}
			}
			return filteredEntries;			
		}		
	}

	public void addEntry(Type type, Entry newEntry) {
		entries.get(type).add(newEntry);
	}

	private static String getJSON(Context context) {
		DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpPost httppost = new HttpPost(API_URL);
		// Depends on your web service
		httppost.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		String jsonText = EMPTY_RESULT;
		
		try {
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();
			// json is UTF-8 by default
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
			StringBuilder stringBuilder = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			jsonText = stringBuilder.toString();

	        setLastSyncDatePreference(context);
		} catch (Exception e) {
			Log.e(Constants.LOG_TAG, e.getMessage());
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}					
			} catch (Exception squish) {
				Log.e(Constants.LOG_TAG, squish.getMessage());
			}
		}
		return jsonText;
	}

	private static void setLastSyncDatePreference(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor prefEditor = sharedPrefs.edit();
		prefEditor.putString(BasicPreferenceActivity.PREFERENCES_SYNC_DATE, now());
		prefEditor.commit();
	}
	private static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW,Locale.getDefault());
		return sdf.format(cal.getTime());
	}
	@Override
	public String toString() {
		return "Model [" + (entries != null ? "entries=" + entries : "") + "]";
	}
}
