package com.ToeTactics.tictactictactoe.database;

public class TPlayer {
	public TPlayer(){
		
	}
	public TPlayer(String id, String fb_id, String fb_name){
		user_id = id;
		facebook_id = fb_id;
		name = fb_name;
	}
	
	String user_id;
	String facebook_id;
	String name;
	char x_or_o;
}
