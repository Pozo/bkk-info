package com.github.pozo.bkkinfo.activities;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.services.NotificationService;
import com.github.pozo.bkkinfo.shared.Constants;
import com.github.pozo.bkkinfo.shared.NetworkConnectionHelper;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class BasicPreferenceActivity extends PreferenceActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

	@Override
	protected void onPause() {
		super.onPause();
		if(NetworkConnectionHelper.isNetworkConnected(this)) {
			stopService(new Intent(this, NotificationService.class));
			startService(new Intent(this, NotificationService.class));			
		}
	}
}
