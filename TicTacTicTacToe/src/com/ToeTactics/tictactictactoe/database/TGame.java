package com.ToeTactics.tictactictactoe.database;

import org.json.JSONArray;

public class TGame {
	public String obj_id;
	public TPlayer player1;
	public TPlayer player2;
	public String current_player_id;
	public JSONArray board;
	public JSONArray chat_log;
	
	public TGame(String objectId){
		obj_id = objectId;
	}
	
	public void SwapPlayers(){
		if(current_player_id.equals(player1.facebook_id))
			current_player_id = player2.facebook_id;
		else
			current_player_id = player1.facebook_id;
	}
}
