package com.github.pozo.bkkinfo.tasks;

import com.github.pozo.bkkinfo.NotificationSettingsActivity;
import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.db.DbConnector;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class TruncateDatabaseTask extends AsyncTask<Void, Void, Void> {
	private final ProgressDialog progressDialog;
	private final NotificationSettingsActivity notificationSettingsActivity;

	public TruncateDatabaseTask(NotificationSettingsActivity notificationSettingsActivity) {
		this.notificationSettingsActivity = notificationSettingsActivity;
		this.progressDialog = new ProgressDialog(notificationSettingsActivity);
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		this.progressDialog.setMessage(notificationSettingsActivity.getResources().getString(R.string.loading));
		this.progressDialog.show();			
	}
	@Override
	protected Void doInBackground(Void... params) {
		DbConnector databaseConnection = DbConnector.getInstance(notificationSettingsActivity);
		databaseConnection.deleteAllRequiredLines();
		databaseConnection.deleteAllNotifications();
		return null;
	}
	@Override
	protected void onPostExecute(Void args) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		notificationSettingsActivity.finish();
		notificationSettingsActivity.startActivity(notificationSettingsActivity.getIntent());
	}
}