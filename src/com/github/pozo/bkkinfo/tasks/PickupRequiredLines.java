package com.github.pozo.bkkinfo.tasks;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.db.DbConnector;
import com.github.pozo.bkkinfo.model.Line;

public class PickupRequiredLines extends AsyncTask<Void, Void, ArrayList<String>>{
	private final ProgressDialog progressDialog;
	private final Context context;
	private final ArrayList<String> lines = new ArrayList<String>();

	public PickupRequiredLines(Context context, ArrayList<Line> entryLines) {
		this.context = context;
		this.progressDialog = new ProgressDialog(context);
		
		for (Line line : entryLines) {					
			lines.addAll(line.getLines());
		}
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		this.progressDialog.setMessage(context.getResources().getString(R.string.loading));
		this.progressDialog.show();			
	}
	@Override
	protected ArrayList<String> doInBackground(Void... params) {
		DbConnector databaseConnection = DbConnector.getInstance(context);
		return databaseConnection.getRequiredLines();
	}
	@Override
	protected void onPostExecute(ArrayList<String> currentRequiredLines) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		new PickupRequiredLineDialog(context, lines, currentRequiredLines).createDialog().show();
	}	
}
