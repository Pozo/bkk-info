package com.github.pozo.bkkinfo.tasks;

import java.util.ArrayList;
import org.json.JSONException;
import com.github.pozo.bkkinfo.MainActivity;
import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.model.Entry;
import com.github.pozo.bkkinfo.model.Model;
import com.github.pozo.bkkinfo.shared.NetworkConnectionHelper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

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

		if(!NetworkConnectionHelper.isNetworkConnected(mainActivity)) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainActivity);
			alertDialogBuilder.setMessage(mainActivity.getResources().getString(R.string.network_warning_description));
			alertDialogBuilder.setCancelable(true);
			alertDialogBuilder.setTitle(mainActivity.getResources().getString(R.string.network_warning_title));
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.setCanceledOnTouchOutside(true);
			alertDialog.show();
			cancel(true);
			return;
		}
		if(!Model.isCached() || refresh) {
			this.progressDialog.setMessage(mainActivity.getResources().getString(R.string.loading));
			this.progressDialog.show();			
		}	
	}
	@Override
	protected Model doInBackground(Void... params) {
		try {
			model = Model.getModel(refresh);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return model;
	}
	@Override
	protected void onPostExecute(Model result) {
		ArrayList<Entry> filteredActiveList = result.getFilteredActiveList(this.mainActivity);

		mainActivity.resetTables();
		
		if (filteredActiveList.isEmpty()) {
			mainActivity.setActiveTableLayoutEmpty();
		} else {
			for (Entry entry : filteredActiveList) {
				mainActivity.addEntryToActiveTable(entry);
			}	
		}
		
		ArrayList<Entry> filteredSoonList = result.getFilteredSoonList(this.mainActivity);
		
		if (filteredSoonList.isEmpty()) {
			mainActivity.setSoonTableLayoutEmpty();
		} else {
			for (Entry entry : filteredSoonList) {
				mainActivity.addEntryToSoonTable(entry);
			}	
		}
		
		ArrayList<Entry> filteredFutureList = result.getFilteredFutureList(this.mainActivity);
		
		if(filteredFutureList.isEmpty()) {
			mainActivity.setFutureTableLayoutEmpty();
		} else {
			for (Entry entry : filteredFutureList) {
				mainActivity.addEntryToFutureTable(entry);
			}			
		}

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}