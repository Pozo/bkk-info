package com.github.pozo.bkkinfo.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.github.pozo.bkkinfo.MainActivity;
import com.github.pozo.bkkinfo.model.Entry.EntryParser;
import com.github.pozo.bkkinfo.shared.Constants;

public class Model {
	private static Model model = null;
	
	public enum Type {
		ACTIVE,SOON,FUTURE;
	};
	private ArrayList<Entry> active = new ArrayList<Entry>();
	private ArrayList<Entry> soonList = new ArrayList<Entry>();
	private ArrayList<Entry> futureList = new ArrayList<Entry>();
	
	public static boolean isCached() {
		return model != null;
	}
	public static synchronized Model getModel(boolean refresh) throws JSONException {
		if(model == null || refresh) {
			String json = getJSON();
			JSONObject jObject = new JSONObject(json);
			model = ModelParser.parse(jObject);
		}
		return model;
	}
	public ArrayList<Entry> getAllEntry() {
		ArrayList<Entry> allEntry = new ArrayList<Entry>();
		
		allEntry.addAll(active);
		allEntry.addAll(soonList);
		allEntry.addAll(futureList);
		
		return allEntry;
	}
	
	public static class ModelParser {
		public static Model parse(JSONObject jsonObject) throws JSONException {
			Model retval = new Model();
			
			parseActiveList(jsonObject, retval);
			parseSoonList(jsonObject, retval);			
			parseFutureList(jsonObject, retval);
			
			return retval;
		}

		private static void parseActiveList(JSONObject jsonObject, Model retval)
				throws JSONException {
			JSONArray activeObjects = jsonObject.getJSONArray(Type.ACTIVE.name().toLowerCase());

			for (int i = 0; i < activeObjects.length(); i++) {
				JSONObject oneObject = activeObjects.getJSONObject(i);
				retval.addActiveEntry(EntryParser.parse(oneObject));
			}
		}

		private static void parseSoonList(JSONObject jsonObject, Model retval)
				throws JSONException {
			JSONArray soonObjects = jsonObject.getJSONArray(Type.SOON.name().toLowerCase());

			for (int i = 0; i < soonObjects.length(); i++) {
				JSONObject oneObject = soonObjects.getJSONObject(i);
				retval.addSoonEntry(EntryParser.parse(oneObject));
			}
		}

		private static void parseFutureList(JSONObject jsonObject, Model retval)
				throws JSONException {
			JSONArray futureObjects = jsonObject.getJSONArray(Type.FUTURE.name().toLowerCase());

			for (int i = 0; i < futureObjects.length(); i++) {
				JSONObject oneObject = futureObjects.getJSONObject(i);
				retval.addFutureEntry(EntryParser.parse(oneObject));
			}
		}
	}

	public ArrayList<Entry> getFilteredActiveList(MainActivity mainActivity) {
		
		if(mainActivity.getToggleButtons().isFilteredLinesEmpty()) {
			return active;
		} else {
			ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
			
			for (Entry entry : active) {
				if(entry.hasAtLeastOneLineType(mainActivity.getToggleButtons().getFilteredLines())) {
					filteredEntries.add(entry);
				}
			}
			return filteredEntries;			
		}

	}

	public void addActiveEntry(Entry active) {
		this.active.add(active);
	}

	public ArrayList<Entry> getFilteredSoonList(MainActivity mainActivity) {
		if(mainActivity.getToggleButtons().isFilteredLinesEmpty()) {
			return soonList;
		} else {
			ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
			
			for (Entry entry : soonList) {
				if(entry.hasAtLeastOneLineType(mainActivity.getToggleButtons().getFilteredLines())) {
					filteredEntries.add(entry);
				}
			}
			return filteredEntries;			
		}
	}

	public void addSoonEntry(Entry soon) {
		this.soonList.add(soon);
	}

	public ArrayList<Entry> getFilteredFutureList(MainActivity mainActivity) {
		if(mainActivity.getToggleButtons().isFilteredLinesEmpty()) {
			return futureList;
		} else {
			ArrayList<Entry> filteredEntries = new ArrayList<Entry>();
			
			for (Entry entry : futureList) {
				if(entry.hasAtLeastOneLineType(mainActivity.getToggleButtons().getFilteredLines())) {
					filteredEntries.add(entry);
				}
			}
			return filteredEntries;			
		}
	}

	public void addFutureEntry(Entry future) {
		this.futureList.add(future);
	}

	@Override
	public String toString() {
		return getClass().getName() + " {\n\t"
				+ (active != null ? "active: " + active + "\n\t" : "")
				+ (soonList != null ? "soonList: " + soonList + "\n\t" : "")
				+ (futureList != null ? "futureList: " + futureList : "")
				+ "\n}";
	}
	private static String getJSON() {
		DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpPost httppost = new HttpPost("http://www.bkk.hu/apps/bkkinfo/lista-api.php");
		// Depends on your web service
		httppost.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		String result = "{\"active\":[],\"soon\":[],\"future\":[]}";
		
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
		return result;
	}
}
