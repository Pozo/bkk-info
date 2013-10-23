package com.github.pozo.bkkinfo.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.pozo.bkkinfo.model.Line.LineParser;
import com.github.pozo.bkkinfo.model.Option.OptionParser;
import com.github.pozo.bkkinfo.model.Time.TimeParser;

public class Entry {
	public static class EntryParser {
		public static Entry parse(JSONObject jsonObject) throws JSONException {
			Entry entry = new Entry();
			
			entry.setId(jsonObject.getString("id"));

			entry.setKezd(TimeParser.parse(jsonObject.getJSONObject(Time.Type.KEZD.name().toLowerCase())));
			
			if(jsonObject.get(Time.Type.VEGE.name().toLowerCase()) != JSONObject.NULL) {
				entry.setVege(TimeParser.parse(jsonObject.getJSONObject(Time.Type.VEGE.name().toLowerCase())));	
			}

			entry.setOption(OptionParser.parse(jsonObject.getJSONObject("opcio")));
			
			entry.setOsszSzoveg(jsonObject.getString("osszSzoveg"));
			entry.setKezdSzoveg(jsonObject.getString("kezdSzoveg"));
			entry.setVegeSzoveg(jsonObject.getString("vegeSzoveg"));
			
			entry.setActive(jsonObject.getBoolean("active"));
			
			JSONArray lineObjects = jsonObject.getJSONArray("jaratokByFajta");

			for (int i = 0; i < lineObjects.length(); i++) {
				entry.addLine(LineParser.parse(lineObjects.getJSONObject(i)));
			}

			entry.setElnevezes(jsonObject.getString("elnevezes"));

			return entry;
		}
	}
	
	private String id;
	
	private Time kezd;
	private Time vege;
	
	private Option opcio;
	
	private String osszSzoveg;
	private String kezdSzoveg;
	private String vegeSzoveg;
	
	private boolean active;
	
	private ArrayList<Line> lines = new ArrayList<Line>();
	
	private String elnevezes;
	
	public boolean addLine(Line line) {
		return this.lines.add(line);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Time getStart() {
		return kezd;
	}

	public void setKezd(Time start) {
		this.kezd = start;
	}

	public Time getEnd() {
		return vege;
	}

	public void setVege(Time end) {
		this.vege = end;
	}

	public Option getOption() {
		return opcio;
	}

	public void setOption(Option option) {
		this.opcio = option;
	}

	public String getOsszSzoveg() {
		return osszSzoveg;
	}

	public void setOsszSzoveg(String osszSzoveg) {
		this.osszSzoveg = osszSzoveg;
	}

	public String getKezdSzoveg() {
		return kezdSzoveg;
	}

	public void setKezdSzoveg(String kezdSzoveg) {
		this.kezdSzoveg = kezdSzoveg;
	}

	public String getVegeSzoveg() {
		return vegeSzoveg;
	}

	public void setVegeSzoveg(String vegeSzoveg) {
		this.vegeSzoveg = vegeSzoveg;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ArrayList<Line> getLines() {
		return lines;
	}
	public void setElnevezes(String elnevezes) {
		this.elnevezes = elnevezes;
		
	}
	public String getElnevezes() {
		return elnevezes;
	}
	@Override
	public String toString() {
		return getClass().getName()
				+ " {\n\t"
				+ (id != null ? "id: " + id + "\n\t" : "")
				+ (kezd != null ? "kezd: " + kezd + "\n\t" : "")
				+ (vege != null ? "vege: " + vege + "\n\t" : "")
				+ (opcio != null ? "opcio: " + opcio + "\n\t" : "")
				+ (osszSzoveg != null ? "osszSzoveg: " + osszSzoveg + "\n\t"
						: "")
				+ (kezdSzoveg != null ? "kezdSzoveg: " + kezdSzoveg + "\n\t"
						: "")
				+ (vegeSzoveg != null ? "vegeSzoveg: " + vegeSzoveg + "\n\t"
						: "") + "active: " + active + "\n\t"
				+ (lines != null ? "lines: " + lines + "\n\t" : "")
				+ (elnevezes != null ? "elnevezes: " + elnevezes : "") + "\n}";
	}

	public boolean hasAtLeastOneLineType(ArrayList<String> filteredLines) {
		for (Line line : getLines()) {
			for (String string : filteredLines) {
				if(line.getType().equalsIgnoreCase(string)) {
					return true;
				}
			}
		}
		return false;
	}
	public String getDescription() {
		if(this.getOption().getGlob_kozl().equals("")) {
			if(this.getOption().getSzabad_szoveg().equals("")) {
				return "";
				
			} else {
				return this.getOption().getSzabad_szoveg();
			}
		} else {
			return this.getOption().getGlob_kozl();			
		}
	}
}
