package com.github.pozo.bkkinfo.services;

import java.util.Timer;

import com.github.pozo.bkkinfo.utils.Constants;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotificationService extends Service {
	private static final String PREF_SYNC_FREQUENCY = "prefSyncFrequency";
	public final static String KEY_RESTART_WITH_REFRESH = "restartWithRefresh";
	
    private Timer timer;
    
	@Override
	public void onCreate() {
		super.onCreate();

		Log.i(Constants.LOG_TAG, "NotificationService:onCreate");
		
		timer = new Timer();
		int refreshFrequencyNumber = getRefreshFrequency();
		
		if(refreshFrequencyNumber != 0) {
			int refreshTimeInMilis = refreshFrequencyNumber * 1000;
			Log.i(Constants.LOG_TAG, String.format("NotificationTimerTask refresh rate is : %d", refreshTimeInMilis));
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
		Log.i(Constants.LOG_TAG, "NotificationService:onStartCommand");

		if(intent.getBooleanExtra(NotificationService.KEY_RESTART_WITH_REFRESH, false)) {
			timer.schedule(new NotificationTimerTask(this), 0);			
		}
		return START_STICKY;		
	}
	
	@Override
	public void onDestroy() {
		if(timer != null) {
			timer.cancel();
			timer.purge();			
		}
		Log.i(Constants.LOG_TAG, "NotificationService:onDestroy");
		super.onDestroy();
	}
	@Override
	
	public IBinder onBind(Intent intent) {

		return null;
	}
}
