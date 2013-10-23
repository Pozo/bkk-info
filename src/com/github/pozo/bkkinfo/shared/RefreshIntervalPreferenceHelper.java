package com.github.pozo.bkkinfo.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RefreshIntervalPreferenceHelper {
	public enum Intervals {
		I_30M("30m",1800),
		I_1H("1h",3600),
		I_2H("2h",7200);
		
		public final String name;
		public final int interval;

		Intervals(String name, int interval) {
			this.name = name;
			this.interval = interval;
		}
		
	}
	public final static String PREFERENCE_REFRESH_INTERVAL = "refresh_interval";

	private static SharedPreferences handler;
	
	private static void getHandler(Context context) {
		if(handler == null) {
			handler = PreferenceManager.getDefaultSharedPreferences(context);
		}
	}
	public static int get(Context context) {
		getHandler(context);
		return handler.getInt(
				PREFERENCE_REFRESH_INTERVAL, 
				Intervals.I_1H.interval);
	}
	public static boolean set(Context context,int newValue) {
		getHandler(context);
		return handler.edit().putInt(PREFERENCE_REFRESH_INTERVAL, newValue).commit();
	}
}
