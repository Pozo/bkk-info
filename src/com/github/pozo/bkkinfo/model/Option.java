package com.github.pozo.bkkinfo.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Option {
	public static class OptionParser {
		public static Option parse(JSONObject jsonObject) throws JSONException {
			Option option = new Option();

			if(jsonObject.has("szabad_szoveg")) {
				option.setSzabad_szoveg(jsonObject.getString("szabad_szoveg"));				
			}
			if(jsonObject.has("zavar_ok")) {
				option.setZavar_ok(jsonObject.getString("zavar_ok"));
			} 
			if(jsonObject.has("zavar_tip")) {
				option.setZavar_tip(jsonObject.getString("zavar_tip"));
			}
			if(jsonObject.has("glob_kozl")) {
				option.setGlob_kozl(jsonObject.getString("glob_kozl"));
			}
			return option;
		}
	}
	
	private String zavar_ok = "";
	private String zavar_tip = "";
	private String szabad_szoveg = "";
	private String glob_kozl = "";
	
	public String getZavar_ok() {
		return zavar_ok;
	}
	public void setZavar_ok(String zavar_ok) {
		this.zavar_ok = zavar_ok;
	}
	public String getZavar_tip() {
		return zavar_tip;
	}
	public void setZavar_tip(String zavar_tip) {
		this.zavar_tip = zavar_tip;
	}
	public String getSzabad_szoveg() {
		return szabad_szoveg;
	}
	public void setSzabad_szoveg(String szabad_szoveg) {
		this.szabad_szoveg = szabad_szoveg;
	}
	public String getGlob_kozl() {
		return glob_kozl;
	}
	public void setGlob_kozl(String glob_kozl) {
		this.glob_kozl = glob_kozl;
	}
	@Override
	public String toString() {
		return "Option ["
				+ (zavar_ok != null ? "zavar_ok=" + zavar_ok + ", " : "")
				+ (zavar_tip != null ? "zavar_tip=" + zavar_tip + ", " : "")
				+ (szabad_szoveg != null ? "szabad_szoveg=" + szabad_szoveg
						+ ", " : "")
				+ (glob_kozl != null ? "glob_kozl=" + glob_kozl : "") + "]";
	}
	
}
