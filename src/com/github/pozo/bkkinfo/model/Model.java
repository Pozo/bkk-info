package com.github.pozo.bkkinfo.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.pozo.bkkinfo.MainActivity;
import com.github.pozo.bkkinfo.model.Entry.EntryParser;

public class Model {
	public enum Type {
		ACTIVE,SOON,FUTURE;
	};
	private ArrayList<Entry> active = new ArrayList<Entry>();
	private ArrayList<Entry> soonList = new ArrayList<Entry>();
	private ArrayList<Entry> futureList = new ArrayList<Entry>();
	
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
	
}
