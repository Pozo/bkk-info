package com.github.pozo.bkkinfo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.github.pozo.bkkinfo.R;

public class NetworkConnectionUnavailableDialog {
	public static AlertDialog create(final Activity activity, final boolean closeActivity) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
		alertDialogBuilder.setMessage(activity.getResources().getString(R.string.network_warning_description));
		alertDialogBuilder.setCancelable(!closeActivity);
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (closeActivity) {
							activity.finish();
						}
					}
				});
		alertDialogBuilder.setTitle(activity.getResources().getString(R.string.network_warning_title));
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setCanceledOnTouchOutside(!closeActivity);
		return alertDialog;
	}
}
