package com.github.pozo.bkkinfo.tasks;

import java.util.ArrayList;
import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.activities.main.MainActivity;
import com.github.pozo.bkkinfo.db.DbConnector;
import com.github.pozo.bkkinfo.model.Model;
import com.github.pozo.bkkinfo.model.Model.Type;
import com.github.pozo.bkkinfo.receivers.NotificationReceiverIntent;
import com.github.pozo.bkkinfo.utils.Constants;
import com.github.pozo.bkkinfo.utils.NetworkConnectionHelper;
import com.github.pozo.bkkinfo.utils.NetworkConnectionUnavailableDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class RetriveModelTask extends AsyncTask<Void, Void, Model> {
	private final ProgressDialog progressDialog;
	private final MainActivity mainActivity;
	private boolean refresh = false;

	public RetriveModelTask(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		this.progressDialog = new ProgressDialog(mainActivity);
	}
	public RetriveModelTask(MainActivity mainActivity, boolean refresh) {
		this(mainActivity);
		
		this.refresh = refresh;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if((Model.isNeedUpdate() || refresh) && NetworkConnectionHelper.isNetworkConnected(mainActivity)) {
			this.progressDialog.setMessage(mainActivity.getResources().getString(R.string.loading));
			this.progressDialog.show();
		}	
	}
	@Override
	protected Model doInBackground(Void... params) {
		Log.i(Constants.LOG_TAG, "RetriveModelTask");
		if(refresh) {
			Model.updateModel(mainActivity);
		}
		DbConnector databaseConnection = DbConnector.getInstance(mainActivity);
		
		ArrayList<String> requiredLines = databaseConnection.getRequiredLines();
		ArrayList<String> notifications = databaseConnection.getNotifications();

		Intent intent = new NotificationReceiverIntent(requiredLines, notifications);
		
		mainActivity.sendBroadcast(intent);

		return Model.getModel(mainActivity);
	}
	@Override
	protected void onPostExecute(Model result) {
		if(refresh && !NetworkConnectionHelper.isNetworkConnected(mainActivity)) {
			NetworkConnectionUnavailableDialog.create(mainActivity, false).show();
		}
		mainActivity.resetTables();

		ArrayList<String> filteredLines = mainActivity.getToggleButtons().getFilteredLines();
		
		mainActivity.fillTable(Type.ACTIVE, result.getFilteredList(Type.ACTIVE, filteredLines));
		mainActivity.fillTable(Type.SOON, result.getFilteredList(Type.SOON, filteredLines));
		mainActivity.fillTable(Type.FUTURE, result.getFilteredList(Type.FUTURE, filteredLines));

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}