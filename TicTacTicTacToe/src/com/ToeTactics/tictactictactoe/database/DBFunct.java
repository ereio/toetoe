package com.ToeTactics.tictactictactoe.database;

import org.json.JSONObject;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.content.Context;
import android.util.Log;

public class DBFunct {
	private static final String TAG = "DBFunct";
	
	private static final String  app_id = "DxcdJQV6VVy4zAb0rlMg4GaX79WBiCCcj9hSbBeV";
	private static final String client_key = "AMaNStYBqXWP6GSzSydLWGlXMzF9zs8BivYVbBkT";
	
	public static void initDB(Context c){
		Parse.initialize(c, app_id, client_key);
		// Also in this method, specify a default Activity to handle push notifications
		//PushService.setDefaultPushCallback(this, YourActivity.class);
	}
	
	////////////////////////////////////////////////////////////////
	// inserts game into db
	// call if game not already in db
	////////////////////////////////////////////////////////////////
	public static void startGame(TGame game, String opponent_fb_id){
		
	}
	
	///////////////////////////////////////
	// creates a user
	// call if user not already logged in
	// use fb credentials
	///////////////////////////////////////
	public static boolean createUser(String fb_id, String username, String password, String email){
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		
		try{
			user.signUp();
			return true;
		} catch(Exception e){
			Log.e(TAG,e.toString());
			return false;
		}
	}
	
	public static boolean signIn(String username, String password){
		try {
			ParseUser.logIn(username,password);
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			return false;
		}
	}
	
	public static TGame ParseObjectToTGame(ParseObject p_obj){
		TGame game_obj = new TGame();
		
		return game_obj;
	}
	
	public static ParseObject TGameToParseObject(TGame game){
		ParseObject p_obj = new ParseObject("Game");
		
		return p_obj;
	}
	
	public static TGame ParseObjectToTPlayer(ParseObject p_obj){
		TGame game_obj = new TGame();
		
		return game_obj;
	}
	
	public static ParseObject TPlayerToParseObject(TPlayer player){
		ParseObject j_obj = new ParseObject("Player");
		
		return j_obj;
	}
}
