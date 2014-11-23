package com.ToeTactics.tictactictactoe.database;

import com.parse.Parse;

import android.content.Context;

public class DBFunct {
	private static final String  app_id = "DxcdJQV6VVy4zAb0rlMg4GaX79WBiCCcj9hSbBeV";
	private static final String client_key = "AMaNStYBqXWP6GSzSydLWGlXMzF9zs8BivYVbBkT";
	
	public static void initDB(Context c){
		Parse.initialize(c, app_id, client_key);
		// Also in this method, specify a default Activity to handle push notifications
		//PushService.setDefaultPushCallback(this, YourActivity.class);
	}
}
