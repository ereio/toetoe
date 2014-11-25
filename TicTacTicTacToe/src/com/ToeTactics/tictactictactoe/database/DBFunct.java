package com.ToeTactics.tictactictactoe.database;

import java.util.List;

import org.json.JSONArray;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.Context;
import android.util.Log;

public class DBFunct {
	private static final String TAG = "DBFunct";
	
	private static final String  APP_ID = "DxcdJQV6VVy4zAb0rlMg4GaX79WBiCCcj9hSbBeV";
	private static final String CLIENT_KEY = "AMaNStYBqXWP6GSzSydLWGlXMzF9zs8BivYVbBkT";
	
	public static final String EMPTY_JSON_BOARD = 
			"[[" +
			"[[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"]]," +
			"[[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"]]," +
			"[[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"]]" +
			"]," +
			"[" +
			"[[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"]]," +
			"[[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"]]," +
			"[[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"]]" +
			"]," +
			"[" +
			"[[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"]]," +
			"[[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"]]," +
			"[[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"],[\"_\",\"_\",\"_\"]]" +
			"]]"; 
	
	public static boolean initDB(Context c){
		try{
			Parse.initialize(c, APP_ID, CLIENT_KEY);
			return true;
		} catch(Exception e){
			Log.e(TAG,e.toString());
			return false;
		}
		// Also in this method, specify a default Activity to handle push notifications
		//PushService.setDefaultPushCallback(this, YourActivity.class);
	}
	
	//-----------------------------------------------------
	// start game with opponent
	// returns current game if game already exists
	// null if error
	//-----------------------------------------------------
	public static TGame startGame(TPlayer opponent){
		if(!isGame(getUser().facebook_id, opponent.facebook_id)
				&& !isGame(opponent.facebook_id, getUser().facebook_id)){
			try{
				// Initialize a new game
				TGame game = new TGame();
				game.board = new JSONArray(EMPTY_JSON_BOARD);
				game.current_player_id = getUser().facebook_id;
				game.player1 = new TPlayer(getUser().facebook_id,
											getUser().name);
				game.player1.x_or_o = 'X';
				opponent.x_or_o = 'O';
				game.player2 = opponent;
				
				ParseObject p_game = TGameToParseObject(game);
				p_game.saveInBackground();
				
				//save object id
				game.obj_id = p_game.getObjectId();
				
				return game;
			} catch(Exception e){
				Log.e(TAG,e.toString());
				return null;
			}
		}
		else {
			ParseObject game_pobj = findGameByPlayers(getUser(), opponent);
			if(game_pobj == null){
				game_pobj = findGameByPlayers(opponent, getUser());
				if(game_pobj == null)
					return null;
			}
			return ParseObjectToTGame(game_pobj);
		}
	}
	
	//-----------------------------------------------------
	// checks if game instance exists
	// using player1 and player2
	//
	// note: Run once to see if player1
	//   has started a game with player2.
	//   Run twice with parameters switched
	//   to check if a game between the 2 players exists
	//-----------------------------------------------------
	public static boolean isGame(String player1_fb_id, String player2_fb_id){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("player1_fb_id", player1_fb_id);
		query.whereEqualTo("player2_fb_id", player2_fb_id);
		
		try{
			List<ParseObject> obj_list = query.find();
			if(obj_list.size() != 1)
				return false;
			else
				return true;
		} catch(Exception e){
			Log.e(TAG,e.toString());
			return false;
		}
	}
	
	//------------------------------------------------------------------
	// Finds ParseObject of game between player 1 and player 2
	//------------------------------------------------------------------
	public static ParseObject findGameByPlayers(TPlayer p1, TPlayer p2){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
		query.whereEqualTo("player1_fb_id", p1.facebook_id);
		query.whereEqualTo("player2_fb_id", p2.facebook_id);
		
		try{
			List<ParseObject> obj_list = query.find();
			if(obj_list.size() != 1)
				return null;
			else
				return obj_list.get(0);
		} catch(Exception e){
			Log.e(TAG,e.toString());
			return null;
		}
	}
	
	//-------------------------------------------------------------------
	// Updates the given game in the database
	//-------------------------------------------------------------------
	public static boolean updateGame(TGame game){
		ParseObject p_game = findGameByPlayers(game.player1, game.player2);
		if(p_game == null){
			p_game = findGameByPlayers(game.player2, game.player1);
		}
		
		if(p_game != null){
			
			
			return true;
		}
		else 
			return false;
	}
	
	//-----------------------------------------------------
	// creates a user
	// call if user not already logged in
	// use fb credentials
	//-----------------------------------------------------
	public static boolean createUser(String fb_id, String username, String password, String email){
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		
		user.put("facebook_id", fb_id);
		
		try{
			user.signUp();
			return true;
		} catch(Exception e){
			Log.e(TAG,e.toString());
			return false;
		}
	}
	
	//-----------------------------------------------------
	// sign user in
	// use facebook credentials
	//-----------------------------------------------------
	public static boolean signIn(String username, String password){
		try {
			ParseUser.logIn(username,password);
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			return false;
		}
	}
	
	//-----------------------------------------------------
	// returns TPlayer instance of current user
	//-----------------------------------------------------
	public static TPlayer getUser(){
		ParseUser p_usr = ParseUser.getCurrentUser();
		
		return new TPlayer(p_usr.getString("facebook_id"),
							p_usr.getUsername());
	}
	
	//===============================================================
	//
	// conversion functions
	//
	//===============================================================
	
	public static TGame ParseObjectToTGame(ParseObject p_obj){
		TGame game_obj = new TGame();
		try{
			ParseQuery<ParseUser> p1_query = ParseUser.getQuery();
			p1_query.whereEqualTo("facebook_id", p_obj.getString("player1_fb_id"));
		
			ParseQuery<ParseUser> p2_query = ParseUser.getQuery();
			p2_query.whereEqualTo("facebook_id", p_obj.getString("player2_fb_id"));

			game_obj.player1 = ParseUserToTPlayer(p1_query.find().get(0));
			game_obj.player2 = ParseUserToTPlayer(p2_query.find().get(0));
			
			game_obj.player1.x_or_o = p_obj.getString("player1_x_o").charAt(0);
			game_obj.player2.x_or_o = p_obj.getString("player2_x_o").charAt(0);

			game_obj.current_player_id = p_obj.getString("current_player");
			game_obj.board = p_obj.getJSONArray("board");
			
			game_obj.obj_id = p_obj.getObjectId();
		} catch(Exception e){
			game_obj = null;
			Log.e(TAG, e.toString());
		}
		
		return game_obj;
	}
	
	public static ParseObject TGameToParseObject(TGame game){
		ParseObject p_obj = new ParseObject("Game");
		
		p_obj.put("board", game.board);
		p_obj.put("current_player", game.current_player_id);
		p_obj.put("player1_fb_id", game.player1.facebook_id);
		p_obj.put("player1_x_o", game.player1.x_or_o);
		p_obj.put("player2_fb_id", game.player2.facebook_id);
		p_obj.put("player2_x_o", game.player2.x_or_o);
		
		return p_obj;
	}
	
	public static TPlayer ParseUserToTPlayer(ParseUser p_user){
		TPlayer player_obj = new TPlayer();
		
		player_obj.facebook_id = p_user.getString("facebook_id");
		player_obj.name = p_user.getUsername();
		
		return player_obj;
	}
}
