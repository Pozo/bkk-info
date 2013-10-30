package com.github.pozo.bkkinfo.tasks;

import java.util.ArrayList;

import org.json.JSONException;
import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.activities.MainActivity;
import com.github.pozo.bkkinfo.model.Model;
import com.github.pozo.bkkinfo.model.Model.Type;
import com.github.pozo.bkkinfo.receivers.NetworkStateReceiver;
import com.github.pozo.bkkinfo.services.NotificationService;
import com.github.pozo.bkkinfo.shared.Constants;
import com.github.pozo.bkkinfo.shared.NetworkConnectionHelper;
import com.github.pozo.bkkinfo.shared.NetworkConnectionUnavailableDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class RetriveModelTask extends AsyncTask<Void, Void, Model> {
	private final ProgressDialog progressDialog;
	private final MainActivity mainActivity;
	private boolean refresh = false;
	private Model model;

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

		if((!Model.isExists() || Model.isEmpty() || refresh) && NetworkConnectionHelper.isNetworkConnected(mainActivity)) {
			this.progressDialog.setMessage(mainActivity.getResources().getString(R.string.loading));
			this.progressDialog.show();
		}	
	}
	@Override
	protected Model doInBackground(Void... params) {
		try {
			model = Model.getModel(refresh);
			
			//LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mainActivity);
	        Intent intent = new Intent();
	        intent.setAction(NetworkStateReceiver.ACTION_NETWORK_STATE);
        
	        Intent intent2 = new Intent();
	        intent2.setAction(NotificationService.ACTION_CHECK_NOTIFICATIONS);

	        mainActivity.sendBroadcast(intent);
	        //mainActivity.sendBroadcast(intent2);
	        //localBroadcastManager.sendBroadcast(intent);
		} catch (JSONException e) {
			Log.e(Constants.LOG_TAG, e.getMessage());
		}
		return model;
	}
	@Override
	protected void onPostExecute(Model result) {
		if(Model.isEmpty() && !NetworkConnectionHelper.isNetworkConnected(mainActivity)) {
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