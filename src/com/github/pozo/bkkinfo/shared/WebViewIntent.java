package com.github.pozo.bkkinfo.shared;

import com.github.pozo.bkkinfo.activities.WebActivity;

import android.content.Context;
import android.content.Intent;

public class WebViewIntent extends Intent {
	private final static String KEY_LINE_ID = "direction";
	
	public static Intent buildIntent(Context context, String id) {
		String packageName = context.getPackageName();
		
		Intent webViewintent = new Intent(context, WebActivity.class);
		webViewintent.setPackage(packageName);
		webViewintent.setAction(Long.toString(System.currentTimeMillis()));
		webViewintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		webViewintent.putExtra(KEY_LINE_ID, id);
		
		return webViewintent;
	}
	public static String getLineId(Intent intent) {
		return intent.getStringExtra(KEY_LINE_ID);
	}
}
