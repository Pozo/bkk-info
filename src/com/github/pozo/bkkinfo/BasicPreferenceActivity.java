package com.github.pozo.bkkinfo;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class BasicPreferenceActivity extends PreferenceActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
