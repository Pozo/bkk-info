package com.github.pozo.bkkinfo.receivers;

import java.util.ArrayList;

import com.github.pozo.bkkinfo.activities.BasicPreferenceActivity;
import com.github.pozo.bkkinfo.activities.web.WebActivityIntent;
import com.github.pozo.bkkinfo.db.DbConnector;
import com.github.pozo.bkkinfo.model.Entry;
import com.github.pozo.bkkinfo.model.Line;
import com.github.pozo.bkkinfo.model.Model;
import com.github.pozo.bkkinfo.utils.Constants;

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
	public static final String JSON_TEXT = "jsonText";
	
	public static final String ACTION_NOTIFICATION = "com.github.pozo.bkkinfo.CHECK_NOTIFICATIONS";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(Constants.LOG_TAG, "NotificationReceiver:onReceive");

		ArrayList<String> requiredLines = NotificationReceiverIntent.getRequiredLines(intent);
		ArrayList<String> notifications = NotificationReceiverIntent.getNotifications(intent);

		Model model = Model.getModel(context);
		
		for (Entry entry : model.getAllEntry()) {

			for (Line line : entry.getLines()) {
				for (String lineName : line.getLines()) {
					if (requiredLines.contains(lineName) && !notifications.contains(entry.getId())) {
						Log.i(Constants.LOG_TAG, "notify : "+lineName);
						createNotification(context,entry,lineName,line.getType());
						
						DbConnector.getInstance(context).addNotification(entry.getId());
					}
				}
			}
		}
	}

    public void createNotification(Context context, Entry entry, String lineName, String lineType) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        int notificationId = Integer.parseInt(entry.getId());
        String title = lineName + " " + entry.getElnevezes();
        
        Notification notification = new Notification(LineResourceHelper.getResourceByType(lineType), title, System.currentTimeMillis());
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        
        if(sharedPrefs.getBoolean(BasicPreferenceActivity.PREFERENCES_NOTIFICATION_SOUND, false)) {
                notification.defaults |= Notification.DEFAULT_SOUND;                        
        }
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;

        notification.when = 0;
        Intent webViewIntent = WebActivityIntent.buildIntent(context, entry.getId());

        notification.setLatestEventInfo(context, title, entry.getOsszSzoveg(), PendingIntent.getActivity(context, 1, webViewIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, notification);
    }
}
