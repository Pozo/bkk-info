package com.github.pozo.bkkinfo.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Line {
	public static class LineParser {
		public static Line parse(JSONObject jsonObject) throws JSONException {
			Line line = new Line();
			
			line.setType(jsonObject.getString("type"));
			
			JSONArray lineObjects = jsonObject.getJSONArray("jaratok");

			for (int i = 0; i < lineObjects.length(); i++) {
				line.addLine(lineObjects.getString(i));
			}
			
			return line;
		}
	}
	public enum Type {
		METRO(0x1),
		HEV(0x2),
		HAJO(0x4),
		VILLAMOS(0x8),
		TROLIBUSZ(0x10),
		BUSZ(0x20),
		EJSZAKAI(0x40),
		SIKLO(0x80),
		LIBEGO(0x100);
		
		private final int flagValue;
		
		Type(int flagValue) {
			this.flagValue = flagValue;
		}

		public int getFlagValue() {
			return flagValue;
		}
	}
	private String type;
	private ArrayList<String> lines = new ArrayList<String>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean addLine(String line) {
		return this.lines.add(line);
	}
	public ArrayList<String> getLines() {
		return lines;
	}
	@Override
	public String toString() {
		return getClass().getName() + " {\n\t"
				+ (type != null ? "type: " + type + "\n\t" : "")
				+ (lines != null ? "lines: " + lines : "") + "\n}";
	}
	
}
