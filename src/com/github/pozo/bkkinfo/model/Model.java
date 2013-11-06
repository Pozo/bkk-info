package com.github.pozo.bkkinfo.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import android.util.Log;

import com.github.pozo.bkkinfo.model.Entry.EntryParser;
import com.github.pozo.bkkinfo.shared.Constants;

public class Model {
	private static final String API_URL = "http://www.bkk.hu/apps/bkkinfo/lista-api.php";
	private static final String EMPTY_RESULT = "{\"active\":[],\"soon\":[],\"future\":[]}";
	
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
	
	public static boolean isExists() {
		return model != null;
	}
	public static boolean isEmpty() {
		if(isExists()) {
			return model.getAllEntry().isEmpty();
		}
		return true;
	}
	public static synchronized Model getModel(String receivedJSON, boolean refresh) throws JSONException {
		if(!isExists() || isEmpty() || refresh) {
			JSONObject jObject = new JSONObject(receivedJSON);
			model = ModelParser.parse(jObject);
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

	public static String getJSON() {
		DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpPost httppost = new HttpPost(API_URL);
		// Depends on your web service
		httppost.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		String result = EMPTY_RESULT;
		
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
			result = stringBuilder.toString();
		} catch (Exception e) {
			
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}					
			} catch (Exception squish) {
				
			}
		}
		return result;
	}
	@Override
	public String toString() {
		return "Model [" + (entries != null ? "entries=" + entries : "") + "]";
	}
}
