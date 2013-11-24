package com.github.pozo.bkkinfo.services;

import java.util.ArrayList;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.pozo.bkkinfo.db.DbConnector;
import com.github.pozo.bkkinfo.model.Model;
import com.github.pozo.bkkinfo.receivers.NotificationReceiverIntent;
import com.github.pozo.bkkinfo.utils.Constants;

public final class NotificationTimerTask extends TimerTask {
	private final Context context;

	public NotificationTimerTask(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		Log.i(Constants.LOG_TAG, "NotificationTimerTask:run");

		Model.updateModel(context);
		DbConnector databaseConnection = DbConnector.getInstance(context);
		
		ArrayList<String> requiredLines = databaseConnection.getRequiredLines();
		ArrayList<String> notifications = databaseConnection.getNotifications();

		Intent intent = new NotificationReceiverIntent(requiredLines, notifications);
		
		context.sendBroadcast(intent);
	}
}