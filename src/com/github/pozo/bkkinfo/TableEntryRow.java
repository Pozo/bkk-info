package com.github.pozo.bkkinfo;

import com.github.pozo.bkkinfo.model.Entry;
import com.github.pozo.bkkinfo.model.Line;
import com.github.pozo.bkkinfo.shared.LineColorHelper;
import com.github.pozo.bkkinfo.shared.WebViewIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
		this.context = context;
		this.entry = entry;
	}
	public TableEntryRow(Context context) {
		this.context = context;
	}
	public void addEmpty(final TableLayout table) {
		TextView tr = createEmptyTableRow();
		
		table.addView(tr, new TableLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	}
	public void add(final TableLayout activeTable) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout linesRow = (LinearLayout) inflater.inflate(R.layout.table_row, null);
		
		linesRow.setBackgroundResource(R.layout.row_selector);
		linesRow.setPadding(10, 10, 10, 10);
		linesRow.setLayoutParams(new LayoutParams(new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT)));
		
		TableLayout tableView = (TableLayout) linesRow.findViewById(R.id.line_table);
		
		for (Line line : entry.getLines()) {
			TableRow tableRow = createTableRow();
			
			for (int i = 0; i < line.getLines().size(); i++) {
				if(i%2 != 0) {
					createLineTextView(line.getType(), line.getLines().get(i), tableRow, R.id.line_two);
							
					if(i!=0) {
						tableView.addView(tableRow);
						tableRow = createTableRow();
					}					
				} else {
					createLineTextView(line.getType(), line.getLines().get(i), tableRow, R.id.line_one);
					
					if(i == line.getLines().size()-1) {
						tableView.addView(tableRow);
					}
				}
				
			}
		}
	
		TextView textView = (TextView) linesRow.findViewById(R.id.cause);
		textView.setText(entry.getElnevezes());
		
		TextView textViewFromTime = (TextView) linesRow.findViewById(R.id.from_time);
		textViewFromTime.setText(entry.getKezdSzoveg());
		
		if (activeTable.getId() == R.id.active_table) {
			textViewFromTime.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
			textViewFromTime.setTextColor(context.getResources().getColor(R.color.gray));
		}
		
		TextView textViewToTime = (TextView) linesRow.findViewById(R.id.to_time);
		textViewToTime.setText(entry.getVegeSzoveg());
		
		linesRow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView textView = new TextView(context);
				textView.setPadding(15, 15, 15, 15);
				textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
				textView.setTextColor(context.getResources().getColor(R.color.light_gray));
				textView.setBackgroundColor(context.getResources().getColor(R.color.purple));
				textView.setText(getDescription());
				textView.setBackgroundResource(R.layout.table_line_detail_text_selector);
				textView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent webViewIntent = WebViewIntent.buildIntent(context, entry);
						context.startActivity(webViewIntent);
					}
				});

				for (int i = 0; i < activeTable.getChildCount(); i++) {
					if(activeTable.getChildAt(i).equals(linesRow)) {
						if(i+2 > activeTable.getChildCount() || !(activeTable.getChildAt(i+2) instanceof TextView)) {
							activeTable.addView(textView, i+2);
							
							LinearLayout ll = new LinearLayout(context);
							ll.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 1));
							ll.setBackgroundColor(Color.GRAY);

							activeTable.addView(ll, i+3);

					        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
					        animation.setFillBefore(true);
					        ll.startAnimation(animation);
					        
					        textView.startAnimation(animation);
					        
						} else {
							activeTable.removeViewAt(i+2);
							activeTable.removeViewAt(i+2);
						}
					}					
				}
			}
		});
		
		activeTable.addView(linesRow, new TableLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		
		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 1));
		ll.setBackgroundColor(Color.GRAY);
		activeTable.addView(ll);
	}
	private void createLineTextView(String lineType, String lineName, TableRow tableRow, int cellId) {
		TextView textView = (TextView) tableRow.findViewById(cellId);
		textView.setText(lineName);
		correctTextSize(lineName, textView);
		textView.setTextColor(LineColorHelper.getTextColorByType(lineType, lineName));
		
		GradientDrawable bgShape = (GradientDrawable) textView.getBackground();
		bgShape.setColor(LineColorHelper.getColorByNameAndType(context, lineName, lineType));			
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
	private TextView createEmptyTableRow() {
		TextView textView = new TextView(context);		
		textView.setText(EMPTY_TABLE_TEXT);
		textView.setGravity(Gravity.CENTER);
		textView.setPadding(15, 15, 15, 15);
		textView.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
		
		return textView;
	}
	private String getDescription() {
		if(entry.getDescription().equals("")) {
			return ENTRY_DETAILS;
		} else {
			return entry.getDescription() + "\n\n" + ENTRY_DETAILS;
		}
	}
	private void correctTextSize(String lineName, TextView textView) {
		if(lineName.length()>=4) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
		}
		if(lineName.length()>=6) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
		}
	}
}
