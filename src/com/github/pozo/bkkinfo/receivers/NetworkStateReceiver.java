package com.github.pozo.bkkinfo.receivers;

import com.github.pozo.bkkinfo.services.NotificationService;
import com.github.pozo.bkkinfo.shared.NetworkConnectionHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkStateReceiver extends BroadcastReceiver {
	public static final String ACTION_NETWORK_STATE = "com.github.pozo.bkkinfo.NETWORKSTATE";
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(NetworkConnectionHelper.isNetworkConnected(context)) {
			
			
			context.startService(new Intent(context, NotificationService.class));
		} else {

			
			context.stopService(new Intent(context, NotificationService.class));
		}
	}

}
