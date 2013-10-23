package com.github.pozo.bkkinfo.tasks;

import java.util.ArrayList;

import com.github.pozo.bkkinfo.NotificationSettingsActivity;
import com.github.pozo.bkkinfo.db.DbConnector;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class RetriveRequiredLinesTask extends AsyncTask<Void, Void, ArrayList<String>> {
	private final ProgressDialog progressDialog;
	private final NotificationSettingsActivity notificationSettingsActivity;
	
	private ArrayList<String> requiredLines = new ArrayList<String>();	
	

	public RetriveRequiredLinesTask(NotificationSettingsActivity notificationSettingsActivity) {
		this.notificationSettingsActivity = notificationSettingsActivity;
		this.progressDialog = new ProgressDialog(notificationSettingsActivity);
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		this.progressDialog.setMessage("Betöltés...");
		this.progressDialog.show();			
	}
	@Override
	protected ArrayList<String> doInBackground(Void... params) {
		DbConnector databaseConnection = DbConnector.getInstance(notificationSettingsActivity);
		requiredLines = databaseConnection.getRequiredLines();
		return requiredLines;
	}
	@Override
	protected void onPostExecute(ArrayList<String> result) {
		notificationSettingsActivity.createTable(requiredLines);
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}