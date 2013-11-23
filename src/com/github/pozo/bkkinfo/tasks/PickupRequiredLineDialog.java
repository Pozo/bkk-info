package com.github.pozo.bkkinfo.tasks;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.db.DbConnector;

public class PickupRequiredLineDialog {

	private final Context context;
	private final ArrayList<String> optionalLines;
	private final ArrayList<String> savedRequiredLines;
	
	public PickupRequiredLineDialog(Context context,ArrayList<String> optionalLines, ArrayList<String> savedRequiredLines) {
		this.context = context;
		this.optionalLines = optionalLines;
		this.savedRequiredLines = savedRequiredLines;
	}
	public AlertDialog createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getText(R.string.notification_settings_dialog_title));

		final String[] dialogValues = new String[optionalLines.size()];
		boolean[] checkedDialogValues = new boolean[optionalLines.size()];
		
		for (int i = 0; i < optionalLines.size(); i++) {
			dialogValues[i] = optionalLines.get(i);
			if(savedRequiredLines.contains(dialogValues[i])) {
				checkedDialogValues[i] = true;
			}
		}
		builder.setMultiChoiceItems(dialogValues, checkedDialogValues,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
						if(isChecked) {
							DbConnector.getInstance(context).addRequiredLine(dialogValues[indexSelected]);
						} else {
							DbConnector.getInstance(context).removeRequiredLine(dialogValues[indexSelected]);
						}
					}
				})
				.setPositiveButton(context.getResources().getString(R.string.notification_settings_dialog_positive_button_title), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				});/*
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {}
						});
						*/
		return builder.create();
	}

}
