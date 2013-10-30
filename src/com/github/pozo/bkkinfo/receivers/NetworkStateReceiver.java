package com.github.pozo.bkkinfo.receivers;

import com.github.pozo.bkkinfo.services.NotificationService;
import com.github.pozo.bkkinfo.shared.Constants;
import com.github.pozo.bkkinfo.shared.NetworkConnectionHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver {
	public static final String ACTION_NETWORK_STATE = "com.github.pozo.bkkinfo.NETWORKSTATE";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(Constants.LOG_TAG, "NetworkReceiver:onReceive");
		if(NetworkConnectionHelper.isNetworkConnected(context)) {
			Log.i(Constants.LOG_TAG, "network is now ON");
			
			context.startService(new Intent(context, NotificationService.class));
		} else {
			Log.i(Constants.LOG_TAG, "network is now OFF");
			
			context.stopService(new Intent(context, NotificationService.class));
		}
	}

}
