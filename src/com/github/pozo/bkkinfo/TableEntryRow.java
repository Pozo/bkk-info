package com.github.pozo.bkkinfo;

import com.github.pozo.bkkinfo.model.Entry;
import com.github.pozo.bkkinfo.model.Line;
import com.github.pozo.bkkinfo.shared.LineColorHelper;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableEntryRow {
	private static final String EMPTY_TABLE_TEXT = "Nincs a szűrésnek megfelelő tervezett forgalmi változás.";
	private static final String ENTRY_DETAILS = "Tovább...";

	private Entry entry;
	private final Context context;

	public TableEntryRow(Context context, Entry entry) {
		this(context);
		this.entry = entry;
	}
	public TableEntryRow(Context context) {
		this.context = context;
	}
	public void addEmpty(final TableLayout table) {
		TextView textView = createEmptyTableRow();
		
		table.addView(textView, new TableLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	}
	public void add(final TableLayout activeTable) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout entryTableRow = (LinearLayout) inflater.inflate(R.layout.table_row, null);
		
		entryTableRow.setBackgroundResource(R.layout.row_selector);
		entryTableRow.setPadding(10, 10, 10, 10);
		entryTableRow.setLayoutParams(new LayoutParams(new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT)));
		
		TableLayout tableView = (TableLayout) entryTableRow.findViewById(R.id.line_table);
		
		fillEntryTableRowLines(tableView);
	
		TextView textView = (TextView) entryTableRow.findViewById(R.id.cause);
		textView.setText(entry.getElnevezes());
		
		TextView textViewFromTime = (TextView) entryTableRow.findViewById(R.id.from_time);
		textViewFromTime.setText(entry.getKezdSzoveg());
		
		if (activeTable.getId() == R.id.active_table) {
			textViewFromTime.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
			textViewFromTime.setTextColor(context.getResources().getColor(R.color.gray));
		}
		
		TextView textViewToTime = (TextView) entryTableRow.findViewById(R.id.to_time);
		textViewToTime.setText(entry.getVegeSzoveg());
		
		entryTableRow.setOnClickListener(new TableRowClickListener(context, this, entryTableRow, activeTable));
		
		activeTable.addView(entryTableRow, new TableLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		
		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 1));
		ll.setBackgroundColor(Color.GRAY);
		activeTable.addView(ll);
	}
	private void fillEntryTableRowLines(TableLayout table) {
		for (Line line : entry.getLines()) {
			TableRow tableRow = createTableRow();
			
			for (int i = 0; i < line.getLines().size(); i++) {
				if(i%2 != 0) {
					createLineTextView(line.getType(), line.getLines().get(i), tableRow, R.id.line_two);
							
					if(i!=0) {
						table.addView(tableRow);
						tableRow = createTableRow();
					}
				} else {
					createLineTextView(line.getType(), line.getLines().get(i), tableRow, R.id.line_one);
					
					if(i == line.getLines().size()-1) {
						table.addView(tableRow);
					}
				}				
			}
		}
	}
	private TableRow createTableRow() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		TableRow tableRow = (TableRow) inflater.inflate(R.layout.table_line_row, null);
		
		TextView textView = (TextView) tableRow.findViewById(R.id.line_one);
		GradientDrawable bgShape = (GradientDrawable) textView.getBackground();
		bgShape.setColor(Color.TRANSPARENT);
		
		textView = (TextView) tableRow.findViewById(R.id.line_two);
		bgShape = (GradientDrawable) textView.getBackground();
		bgShape.setColor(Color.TRANSPARENT);	
		
		return tableRow;
	}
	private void createLineTextView(String lineType, String lineName, TableRow tableRow, int cellId) {
		TextView textView = (TextView) tableRow.findViewById(cellId);
		textView.setText(lineName);
		correctTextSize(lineName, textView);
		textView.setTextColor(LineColorHelper.getTextColorByType(lineType, lineName));
		
		GradientDrawable bgShape = (GradientDrawable) textView.getBackground();
		bgShape.setColor(LineColorHelper.getColorByNameAndType(context, lineName, lineType));			
	}

	private TextView createEmptyTableRow() {
		TextView textView = new TextView(context);		
		textView.setText(EMPTY_TABLE_TEXT);
		textView.setGravity(Gravity.CENTER);
		textView.setPadding(15, 15, 15, 15);
		textView.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
		
		return textView;
	}
	String getDescription() {
		if(entry.getDescription().equals("")) {
			return ENTRY_DETAILS;
		} else {
			return entry.getDescription() + "\n\n" + ENTRY_DETAILS;
		}
	}
	private void correctTextSize(String lineName, TextView textView) {
		if(lineName.length()>=4) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
			textView.setHeight((int)dipToPixels(context, 23f));
		}
		if(lineName.length()>=6) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
			textView.setHeight((int)dipToPixels(context, 23f));
		}
		
	}
	public static float dipToPixels(Context context, float dipValue) {
	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}
	public Entry getEntry() {
		return entry;
	}
}
