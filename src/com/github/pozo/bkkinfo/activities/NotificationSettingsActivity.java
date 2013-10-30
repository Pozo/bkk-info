package com.github.pozo.bkkinfo.activities;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map.Entry;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.db.DbConnector;
import com.github.pozo.bkkinfo.model.Line.Type;
import com.github.pozo.bkkinfo.shared.AvailableLines;
import com.github.pozo.bkkinfo.shared.LineColorHelper;
import com.github.pozo.bkkinfo.tasks.TruncateDatabaseTask;
import com.github.pozo.bkkinfo.tasks.RetriveRequiredLinesTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class NotificationSettingsActivity extends Activity {
	public static final String CHECKED = "checked";
	private static final int OFFSET = 5;
	
	private int[] ids = new int[] { 
			R.id.line_first, 
			R.id.line_second,
			R.id.line_three, 
			R.id.line_four, 
			R.id.line_five };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_settings);
		
		new RetriveRequiredLinesTask(this).execute();
		
	}
	public void createTable(ArrayList<String> requiredLines) {
		LinearLayout ll = (LinearLayout) findViewById(R.id.lines_table);
		ll.addView(createEmptyTableRow());
		
		for (Entry<Type, String[]> lines : AvailableLines.getLines().entrySet()) {
			
			for (int i = 0; i < lines.getValue().length; i += OFFSET) {
				TableRow tableRow = createTableRow();
				
				ll.addView(tableRow);
				appendRow(i,lines.getKey().name().toLowerCase(Locale.ENGLISH), lines.getValue(), tableRow, requiredLines);
			}
			ll.addView(createEmptyTableRow());
		}
	}
	private void appendRow(int index, String typename, String[] array, TableRow tableRow, ArrayList<String> requiredLines) {
		for (int j = 0; j < OFFSET; j++) {
			if(array.length > j + index) {
				createLineTextView(typename, array[j + index], tableRow, ids[j], requiredLines);
			}
		}
	}
	private void createLineTextView(String lineType, String lineName, TableRow tableRow, int cellId, ArrayList<String> requiredLines) {
		TextView textView = (TextView) tableRow.findViewById(cellId);
		textView.setText(lineName);
		
		textView.setTextColor(LineColorHelper.getTextColorByType(lineType, lineName));
		
		correctTextSize(lineName, textView);
		
		GradientDrawable bgShape = (GradientDrawable) textView.getBackground();
		final int colorByNameAndType = LineColorHelper.getColorByNameAndType(this, lineName, lineType);
		bgShape.setColor(colorByNameAndType);
		
		if(requiredLines.contains(lineName)) {
			setLineTextBackgroundDeselected(colorByNameAndType, textView);
			textView.setTag(CHECKED);
		} else {
			setLineTextBackgroundSelected(colorByNameAndType, textView);
		}
		
		textView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				TextView textView = (TextView) view;
				String tag = (String) textView.getTag();
				DbConnector databaseConnection = DbConnector.getInstance(NotificationSettingsActivity.this);
				
				if(tag != null && tag.equals(CHECKED)) {
					setLineTextBackgroundSelected(colorByNameAndType, view);
					
					databaseConnection.removeRequiredLine(textView.getText().toString());
					
					textView.setTag("");
				} else {
					setLineTextBackgroundDeselected(colorByNameAndType, view);
					
					databaseConnection.addRequiredLine(textView.getText().toString());
					
					textView.setTag(CHECKED);
				}
				

			}
		});
	}
	private void correctTextSize(String lineName, TextView textView) {
		if(lineName.length()>=4) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
		}
		if(lineName.length()>=6) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
		}
	}
	private TableRow createTableRow() {
		LayoutInflater inflater = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		TableRow tableRow = (TableRow) inflater.inflate(R.layout.notification_table_line_row, null);
		
		tableRow.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		tableRow.setGravity(Gravity.CENTER);
		
		for(int id : ids) {
			TextView textView = (TextView) tableRow.findViewById(id);
			GradientDrawable bgShape = (GradientDrawable) textView.getBackground();
			bgShape.setColor(Color.TRANSPARENT);
		}
		
		return tableRow;
	}
	private TableRow createEmptyTableRow() {
		TableRow tableRow = new TableRow(this);
		tableRow.setGravity(Gravity.CENTER);
		tableRow.setPadding(15, 15, 15, 15);
		
		return tableRow;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.notifications, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.remove_selections:
	        	new TruncateDatabaseTask(this).execute();
	        	return true;
	        case R.id.settings:
	        	
	    		String packageName = getPackageName();
	    		
	    		Intent notificationSettingsActivity = new Intent(this, BasicPreferenceActivity.class);
	    		notificationSettingsActivity.setPackage(packageName);
	    		notificationSettingsActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		
	    		startActivity(notificationSettingsActivity);
	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	private void setLineTextBackgroundSelected(final int colorByNameAndType,
			View textView) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setColor(colorByNameAndType);
		drawable.setStroke(0, Color.BLACK);
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setCornerRadius(5f);
		textView.setBackgroundDrawable(drawable);
	}
	private void setLineTextBackgroundDeselected(final int colorByNameAndType,
			View textView) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setColor(colorByNameAndType);
		drawable.setStroke(5, getStrokeColorByBackgroundColor(colorByNameAndType));
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setCornerRadius(5f);
		textView.setBackgroundDrawable(drawable);
	}

	private int getStrokeColorByBackgroundColor(int colorByNameAndType) {
		if((colorByNameAndType == getResources().getColor(R.color.purple))
				|| (colorByNameAndType == getResources().getColor(R.color.h6))
				|| (colorByNameAndType == getResources().getColor(R.color.h7))
				|| (colorByNameAndType == getResources().getColor(R.color.h8))
				|| (colorByNameAndType == getResources().getColor(R.color.d12))
				|| (colorByNameAndType == getResources().getColor(R.color.d13))
				|| (colorByNameAndType == getResources().getColor(R.color.libego))
				|| (colorByNameAndType == Color.BLACK)) {
					
			return Color.RED;
		} else {
			return Color.BLACK;
		}
	}
}
