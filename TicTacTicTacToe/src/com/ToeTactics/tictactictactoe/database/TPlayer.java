package com.ToeTactics.tictactictactoe.database;

public class TPlayer {
	public String facebook_id;
	public String name;
	public char x_or_o;
	
	public TPlayer(){
		
	}
	public TPlayer(String fb_id, String fb_name){
		facebook_id = fb_id;
		name = fb_name;
	}
}
