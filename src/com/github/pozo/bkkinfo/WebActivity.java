package com.github.pozo.bkkinfo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.github.pozo.bkkinfo.shared.WebViewIntent;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends Activity {
	private String lineId;
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i("tag", "onNewIntent" + intent.toString());
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("tag", "onResume");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i("tag", "onStart");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("tag", "onActivityResult" + data.toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		lineId = WebViewIntent.getLineId(getIntent());

		final ProgressDialog pd = ProgressDialog.show(this, "", "Loading...", true);

		final WebView webView = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (pd.isShowing() && pd != null) {
					pd.dismiss();
				}
			}
		});
		Log.i("tag", "onCreate, lineID: " + lineId);
		setWebViewContent("http://m.bkkinfo.hu/alert.php?id=" + lineId, webView);
	}

	private void setWebViewContent(String uri, final WebView webView) {
		try {
			Connection connected = Jsoup.connect(uri);
			
			Document doc = connected.get();
			
			doc.select("div[data-role*=header]").remove();
			doc.select("div[data-role*=footer]").remove();
			
			webView.loadDataWithBaseURL("http://m.bkkinfo.hu/", doc.html(), "text/html", "utf-8","http://m.bkkinfo.hu/");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
