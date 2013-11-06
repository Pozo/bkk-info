package com.github.pozo.bkkinfo.services;

import java.util.ArrayList;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.pozo.bkkinfo.db.DbConnector;
import com.github.pozo.bkkinfo.model.Model;
import com.github.pozo.bkkinfo.receivers.NotificationReceiver;
import com.github.pozo.bkkinfo.shared.Constants;

final class NotificationTimerTask extends TimerTask {
	private final Context context;

	public NotificationTimerTask(Context notificationService) {
		this.context = notificationService;
	}

	@Override
	public void run() {
		Log.i(Constants.LOG_TAG, "NotificationTimerTask:run");
		DbConnector databaseConnection = DbConnector.getInstance(context);
		String jsonText = Model.getJSON();
		
		ArrayList<String> requiredLines = databaseConnection.getRequiredLines();
		ArrayList<String> notifications = databaseConnection.getNotifications();

		Intent intent = new Intent(NotificationReceiver.ACTION_NOTIFICATION);

		intent.putExtra(NotificationReceiver.REQUIRED_LINES, requiredLines);
		intent.putExtra(NotificationReceiver.NOTIFICATIONS, notifications);
		intent.putExtra(NotificationReceiver.JSON_TEXT, jsonText);
		
		context.sendBroadcast(intent);
		//LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
}