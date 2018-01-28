package com.ToeTactics.toetoe.database;

import android.content.Context;
import android.content.Intent;

import com.ToeTactics.toetoe.GameBoard;
import com.parse.ParsePushBroadcastReceiver;

public class ToePushReceiver extends ParsePushBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);
		
		//Toast.makeText(context.getApplicationContext(), "Push Notification Received", Toast.LENGTH_SHORT).show();
		
		// Call activity function
		if(GameBoard.thisActivity != null){
			GameBoard.thisActivity.receivePushMsg(intent);
		}
	}
	
	@Override
	public void onPushReceive(Context context, Intent intent){
		
	}
}
