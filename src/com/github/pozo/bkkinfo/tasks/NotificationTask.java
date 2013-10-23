package com.github.pozo.bkkinfo.tasks;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.pozo.bkkinfo.db.DbConnector;
import com.github.pozo.bkkinfo.model.Entry;
import com.github.pozo.bkkinfo.model.Line;
import com.github.pozo.bkkinfo.model.Model;
import com.github.pozo.bkkinfo.model.Model.ModelParser;
import com.github.pozo.bkkinfo.services.NotificationService;

import android.os.AsyncTask;
import android.util.Log;

public class NotificationTask extends AsyncTask<Void, Void, Model> {
	private final NotificationService notificationService;
	private boolean refresh = false;
	
	private static Model model = null;
	private ArrayList<String> requiredLines = new ArrayList<String>();
	private ArrayList<String> notifications = new ArrayList<String>();
	

	public NotificationTask(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Override
	protected Model doInBackground(Void... params) {
		try {
			if(isNotCached()) {
				String json = RetriveJSON.getJSON();
				JSONObject jObject = new JSONObject(json);
				model = ModelParser.parse(jObject);
			}
			DbConnector databaseConnection = DbConnector.getInstance(notificationService);
			requiredLines = databaseConnection.getRequiredLines();
			notifications = databaseConnection.getNotifications();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return model;
	}
	private boolean isNotCached() {
		return model == null || refresh;
	}
	@Override
	protected void onPostExecute(Model result) {
		Log.i("tag", "NotificationTask:onPostExecute");
		for (Entry entry : model.getAllEntry()) {
			
			for (Line line : entry.getLines()) {
				for (String lineName : line.getLines()) {
					if(requiredLines.contains(lineName) && !notifications.contains(entry.getId())) {
						notificationService.createNotification(entry, lineName, line.getType());
						DbConnector.getInstance(notificationService).addNotification(entry.getId());
					}
				}
			}
		}
	}
}