package com.github.pozo.bkkinfo.receivers;

import java.util.ArrayList;

import org.json.JSONException;

import com.github.pozo.bkkinfo.db.DbConnector;
import com.github.pozo.bkkinfo.model.Entry;
import com.github.pozo.bkkinfo.model.Line;
import com.github.pozo.bkkinfo.model.Model;
import com.github.pozo.bkkinfo.shared.Constants;
import com.github.pozo.bkkinfo.shared.LineResourceHelper;
import com.github.pozo.bkkinfo.shared.WebViewIntent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
	public static final String NOTIFICATIONS = "notifications";
	public static final String REQUIRED_LINES = "requiredLines";
	public static final String JSON_TEXT = "jsonText";
	
	public static final String ACTION_NOTIFICATION = "com.github.pozo.bkkinfo.NOTIFICATION";
	
	private static final String PREFERENCES_NOTIFICATION_SOUND = "notificationSound";
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Log.i(Constants.LOG_TAG, "NotificationReceiver:onReceive");
	
			ArrayList<String> requiredLines = intent.getStringArrayListExtra(REQUIRED_LINES);
			ArrayList<String> notifications = intent.getStringArrayListExtra(NOTIFICATIONS);
			String jsonText = intent.getStringExtra(JSON_TEXT);

			Model model = Model.getModel(jsonText, false);
			
			for (Entry entry : model.getAllEntry()) {

				for (Line line : entry.getLines()) {
					for (String lineName : line.getLines()) {
						if (requiredLines.contains(lineName) && !notifications.contains(entry.getId())) {
							createNotification(context,entry,lineName,line.getType());
							
							DbConnector.getInstance(context).addNotification(entry.getId());
						}
					}
				}
			}
			
		} catch (JSONException e) {
			Log.e(Constants.LOG_TAG, e.getMessage());
		}
	}

    public void createNotification(Context context, Entry entry, String lineName, String lineType) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        int notificationId = Integer.parseInt(entry.getId());
        String title = lineName + " " + entry.getElnevezes();
        
        Notification notification = new Notification(LineResourceHelper.getResourceByType(lineType), title, System.currentTimeMillis());
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        
        if(sharedPrefs.getBoolean(PREFERENCES_NOTIFICATION_SOUND, false)) {
                notification.defaults |= Notification.DEFAULT_SOUND;                        
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;

        notification.when = 0;
        Intent webViewIntent = WebViewIntent.buildIntent(context, entry.getId());

        notification.setLatestEventInfo(context, title, entry.getOsszSzoveg(), PendingIntent.getActivity(context, 1, webViewIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, notification);
    }
}
