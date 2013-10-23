package com.github.pozo.bkkinfo.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Time {
	public static class TimeParser {
		public static Time parse(JSONObject jsonObject) throws JSONException {
			Time time = new Time();
			
			time.setActive(jsonObject.getBoolean("active"));
			time.setEpoch(jsonObject.getString("epoch"));
			time.setEv(jsonObject.getString("ev"));
			time.setExact(jsonObject.getBoolean("exact"));
			time.setHonap(jsonObject.getString("honap"));
			time.setIdo(jsonObject.getString("ido"));
			time.setNap(jsonObject.getString("nap"));
			time.setNapnev(jsonObject.getString("napnev"));
			time.setPnapnev(jsonObject.getString("Pnapnev"));
			
			return time;			
		}
	}
	
	public enum Type {
		KEZD, VEGE;
	};

	private String epoch;
	private boolean exact;
	private boolean active;
	private String ev;
	private String honap;
	private String nap;
	private String napnev;
	private String Pnapnev;
	private String ido;

	public String getEpoch() {
		return epoch;
	}
	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}
	public boolean isExact() {
		return exact;
	}
	public void setExact(boolean exact) {
		this.exact = exact;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getEv() {
		return ev;
	}
	public void setEv(String ev) {
		this.ev = ev;
	}
	public String getHonap() {
		return honap;
	}
	public void setHonap(String honap) {
		this.honap = honap;
	}
	public String getNap() {
		return nap;
	}
	public void setNap(String nap) {
		this.nap = nap;
	}
	public String getNapnev() {
		return napnev;
	}
	public void setNapnev(String napnev) {
		this.napnev = napnev;
	}
	public String getPnapnev() {
		return Pnapnev;
	}
	public void setPnapnev(String pnapnev) {
		Pnapnev = pnapnev;
	}
	public String getIdo() {
		return ido;
	}
	public void setIdo(String ido) {
		this.ido = ido;
	}
	@Override
	public String toString() {
		return getClass().getName() + " {\n\t"
				+ (epoch != null ? "epoch: " + epoch + "\n\t" : "") + "exact: "
				+ exact + "\n\tactive: " + active + "\n\t"
				+ (ev != null ? "ev: " + ev + "\n\t" : "")
				+ (honap != null ? "honap: " + honap + "\n\t" : "")
				+ (nap != null ? "nap: " + nap + "\n\t" : "")
				+ (napnev != null ? "napnev: " + napnev + "\n\t" : "")
				+ (Pnapnev != null ? "Pnapnev: " + Pnapnev + "\n\t" : "")
				+ (ido != null ? "ido: " + ido : "") + "\n}";
	}
	
}
