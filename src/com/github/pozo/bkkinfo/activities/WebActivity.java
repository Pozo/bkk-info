package com.github.pozo.bkkinfo.activities;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.shared.NetworkConnectionUnavailableDialog;
import com.github.pozo.bkkinfo.shared.WebViewIntent;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends Activity {
	private String lineId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		lineId = WebViewIntent.getLineId(getIntent());
	
		final ProgressDialog pd = ProgressDialog.show(this, "", getResources().getString(R.string.loading), true);

		final WebView webView = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}
		});

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

			NetworkConnectionUnavailableDialog.create(WebActivity.this, true).show();
		}
	}
}
