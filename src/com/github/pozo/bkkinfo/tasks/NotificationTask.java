package com.github.pozo.bkkinfo.tasks;

import java.util.ArrayList;
import org.json.JSONException;
import com.github.pozo.bkkinfo.ModelReceiver;
import com.github.pozo.bkkinfo.db.DbConnector;
import com.github.pozo.bkkinfo.model.Model;
import com.github.pozo.bkkinfo.services.NotificationService;
import com.github.pozo.bkkinfo.shared.Constants;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class NotificationTask extends AsyncTask<Void, Void, Model> {
	private final NotificationService notificationService;
	private boolean refresh = false;
	
	private ArrayList<String> requiredLines = new ArrayList<String>();
	private ArrayList<String> notifications = new ArrayList<String>();
	

	public NotificationTask(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Override
	protected Model doInBackground(Void... params) {
		Model model = null;
		try {
			model = Model.getModel(refresh);
			DbConnector databaseConnection = DbConnector.getInstance(notificationService);
			requiredLines = databaseConnection.getRequiredLines();
			notifications = databaseConnection.getNotifications();
		} catch (JSONException e) {
			Log.e(Constants.LOG_TAG, e.getMessage());
		}
		return model;
	}
	@Override
	protected void onPostExecute(Model result) {
		Log.i(Constants.LOG_TAG, "NotificationTask:onPostExecute");
		
		Intent intent = new Intent(ModelReceiver.BROADCAST_ACTION);

		intent.putExtra(ModelReceiver.REQUIRED_LINES, requiredLines);
		intent.putExtra(ModelReceiver.NOTIFICATIONS, notifications);

		LocalBroadcastManager.getInstance(notificationService).sendBroadcast(intent);
	}
}