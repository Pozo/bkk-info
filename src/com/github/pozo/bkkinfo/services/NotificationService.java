package com.github.pozo.bkkinfo.services;

import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class NotificationService extends Service {
	private static final String PREF_SYNC_FREQUENCY = "prefSyncFrequency";
	public static final String ACTION_CHECK_NOTIFICATIONS = "com.github.pozo.bkkinfo.CHECK_NOTIFICATIONS";
	
    private Timer timer;
    
	@Override
	public void onCreate() {
		super.onCreate();

		
		timer = new Timer();
		int refreshFrequencyNumber = getRefreshFrequency();
		
		if(refreshFrequencyNumber != 0) {
			int refreshTimeInMilis = refreshFrequencyNumber * 1000;

			timer.scheduleAtFixedRate(new NotificationTimerTask(this), refreshTimeInMilis, refreshTimeInMilis);
		}
	}
	private int getRefreshFrequency() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String refreshFrequency = sharedPrefs.getString(PREF_SYNC_FREQUENCY, "600");
		int refreshFrequencyNumber = Integer.parseInt(refreshFrequency);
		return refreshFrequencyNumber;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		timer.schedule(new NotificationTimerTask(this), 0);
		return START_STICKY;		
	}
	
	@Override
	public void onDestroy() {
		if(timer != null) {
			timer.cancel();
			timer.purge();			
		}

		super.onDestroy();
	}
	@Override
	
	public IBinder onBind(Intent intent) {

		return null;
	}
}
