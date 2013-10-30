package com.github.pozo.bkkinfo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.pozo.bkkinfo.shared.WebViewIntent;

final class TableRowClickListener implements OnClickListener {
	private final TableEntryRow tableEntryRow;
	private final LinearLayout linesRow;
	private final TableLayout table;

	TableRowClickListener(TableEntryRow tableEntryRow, LinearLayout linesRow, TableLayout table) {
		this.tableEntryRow = tableEntryRow;
		this.linesRow = linesRow;
		this.table = table;
	}

	@Override
	public void onClick(View v) {
		TextView textView = new TextView(this.tableEntryRow.context);
		textView.setPadding(15, 15, 15, 15);
		textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		textView.setTextColor(this.tableEntryRow.context.getResources().getColor(R.color.light_gray));
		textView.setBackgroundColor(this.tableEntryRow.context.getResources().getColor(R.color.purple));
		textView.setText(this.tableEntryRow.getDescription());
		textView.setBackgroundResource(R.layout.table_line_detail_text_selector);
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent webViewIntent = WebViewIntent.buildIntent(TableRowClickListener.this.tableEntryRow.context, TableRowClickListener.this.tableEntryRow.entry.getId());
				TableRowClickListener.this.tableEntryRow.context.startActivity(webViewIntent);
			}
		});

		for (int i = 0; i < table.getChildCount(); i++) {
			if(table.getChildAt(i).equals(linesRow)) {
				if(i+2 > table.getChildCount() || !(table.getChildAt(i+2) instanceof TextView)) {
					table.addView(textView, i+2);
					
					LinearLayout ll = new LinearLayout(this.tableEntryRow.context);
					ll.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 1));
					ll.setBackgroundColor(Color.GRAY);

					table.addView(ll, i+3);

			        Animation animation = AnimationUtils.loadAnimation(this.tableEntryRow.context, R.anim.slide_in);
			        animation.setFillBefore(true);
			        ll.startAnimation(animation);
			        
			        textView.startAnimation(animation);
			        
				} else {
					table.removeViewAt(i+2);
					table.removeViewAt(i+2);
				}
			}					
		}
	}
}