package com.ToeTactics.tictactictactoe.database;

import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;

public class ToePushReceiver extends ParsePushBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);
		
		Toast.makeText(context.getApplicationContext(), "Push Notification Received", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onPushReceive(Context context, Intent intent){
		
	}
}
