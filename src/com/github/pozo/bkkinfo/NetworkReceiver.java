package com.github.pozo.bkkinfo;

import com.github.pozo.bkkinfo.services.NotificationService;
import com.github.pozo.bkkinfo.shared.Constants;
import com.github.pozo.bkkinfo.shared.NetworkConnectionHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(Constants.LOG_TAG, "network status changed");
		if(NetworkConnectionHelper.isNetworkConnected(context)) {
			Log.i(Constants.LOG_TAG, "network is now ON");
			
			context.startService(new Intent(context, NotificationService.class));
		} else {
			Log.i(Constants.LOG_TAG, "network is now OFF");
			
			context.stopService(new Intent(context, NotificationService.class));
		}
	}

}
