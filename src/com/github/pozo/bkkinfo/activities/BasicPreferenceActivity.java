package com.github.pozo.bkkinfo.activities;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.services.NotificationService;
import com.github.pozo.bkkinfo.utils.Constants;
import com.github.pozo.bkkinfo.utils.NetworkConnectionHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class BasicPreferenceActivity extends PreferenceActivity {
	public static final String PREFERENCES_NOTIFICATION_SOUND = "notificationSound";
	public static final String PREFERENCES_SYNC_DATE = "prefLastSyncDate";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Preference prefLastSyncDate = findPreference(PREFERENCES_SYNC_DATE);
        prefLastSyncDate.setSummary(sharedPrefs.getString(PREFERENCES_SYNC_DATE, getResources().getString(R.string.notification_settings_last_refresh_default_value)));
    }

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(Constants.LOG_TAG, "BasicPreferenceActivity:onPause");
		if(NetworkConnectionHelper.isNetworkConnected(this)) {
			stopService(new Intent(this, NotificationService.class));
			
			Intent intent = new Intent(this, NotificationService.class);
			intent.putExtra(NotificationService.KEY_RESTART_WITH_REFRESH, false);
			startService(intent);
		}
	}
}
