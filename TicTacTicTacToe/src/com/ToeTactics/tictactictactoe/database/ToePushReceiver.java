package com.ToeTactics.tictactictactoe.database;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ToeTactics.tictactictactoe.GameBoard;
import com.parse.ParsePushBroadcastReceiver;

public class ToePushReceiver extends ParsePushBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);
		
	}
	
	@Override
	public void onPushReceive(Context context, Intent intent){
		intent = new Intent(context, GameBoard.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		
	}
}
