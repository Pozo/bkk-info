package com.github.pozo.bkkinfo.services;

import java.util.Timer;
import java.util.TimerTask;

import com.github.pozo.bkkinfo.model.Entry;
import com.github.pozo.bkkinfo.shared.LineResourceHelper;
import com.github.pozo.bkkinfo.shared.WebViewIntent;
import com.github.pozo.bkkinfo.tasks.NotificationTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotificationService extends Service {
	private static final String NOTIFICATION_SOUND = "notificationSound";
	private static final String PREF_SYNC_FREQUENCY = "prefSyncFrequency";
	
    private static Timer timer = new Timer();
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i("tag", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Log.i("tag", "onStartCommand");
		String refreshFrequency = sharedPrefs.getString(PREF_SYNC_FREQUENCY, "600");
		int refreshFrequencyNumber = Integer.parseInt(refreshFrequency);
		
		if(refreshFrequencyNumber != 0) {
			Log.i("tag", (refreshFrequencyNumber * 1000)+".");
			timer.scheduleAtFixedRate(new TimerTask() {
				
				@Override
				public void run() {
					new NotificationTask(NotificationService.this).execute();
				}
			}, 0, refreshFrequencyNumber * 1000);
		}


		return START_STICKY;		
	}
	
	@Override
	public void onDestroy() {
		Log.i("tag", "onDestroy");
		super.onDestroy();
	}

	public void createNotification(Entry entry, String lineName, String lineType) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 
		int notificationId = Integer.parseInt(entry.getId());
		String title = lineName + " " + entry.getElnevezes();
		
		Notification notification = new Notification(LineResourceHelper.getResourceByType(lineType), title, System.currentTimeMillis());
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		if(sharedPrefs.getBoolean(NOTIFICATION_SOUND, false)) {
			notification.defaults |= Notification.DEFAULT_SOUND;			
		}
		notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;

		notification.when = 0;
		Intent webViewIntent = WebViewIntent.buildIntent(this, entry);

		notification.setLatestEventInfo(this, title, entry.getOsszSzoveg(), PendingIntent.getActivity(this, 1, webViewIntent, PendingIntent.FLAG_UPDATE_CURRENT));
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(notificationId, notification);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
}
