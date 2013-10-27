package com.github.pozo.bkkinfo.services;

import java.util.ArrayList;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.github.pozo.bkkinfo.ModelReceiver;
import com.github.pozo.bkkinfo.db.DbConnector;

final class NotificationTimerTask extends TimerTask {
	private final Context context;

	public NotificationTimerTask(Context notificationService) {
		this.context = notificationService;
	}

	@Override
	public void run() {
		DbConnector databaseConnection = DbConnector.getInstance(context);
		
		ArrayList<String> requiredLines = databaseConnection.getRequiredLines();
		ArrayList<String> notifications = databaseConnection.getNotifications();

		Intent intent = new Intent(ModelReceiver.BROADCAST_ACTION);

		intent.putExtra(ModelReceiver.REQUIRED_LINES, requiredLines);
		intent.putExtra(ModelReceiver.NOTIFICATIONS, notifications);

		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
}