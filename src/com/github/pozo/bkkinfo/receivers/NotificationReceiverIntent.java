package com.github.pozo.bkkinfo.receivers;


import java.util.ArrayList;

import android.content.Intent;

public class NotificationReceiverIntent extends Intent {
	public static final String KEY_NOTIFICATIONS = "notifications";
	public static final String KEY_REQUIRED_LINES = "requiredLines";
	
	public NotificationReceiverIntent(ArrayList<String> requiredLines, ArrayList<String> notifications) {
		super(NotificationReceiver.ACTION_NOTIFICATION);
		this.putExtra(KEY_REQUIRED_LINES, requiredLines);
		this.putExtra(KEY_NOTIFICATIONS, notifications);
	}
	public static ArrayList<String> getNotifications(Intent intent) {
		return intent.getStringArrayListExtra(KEY_NOTIFICATIONS);
	}
	public static ArrayList<String> getRequiredLines(Intent intent) {
		return intent.getStringArrayListExtra(KEY_REQUIRED_LINES);
	}
}
