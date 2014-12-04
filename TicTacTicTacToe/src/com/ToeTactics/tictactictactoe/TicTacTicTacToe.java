package com.ToeTactics.tictactictactoe;

import com.ToeTactics.tictactictactoe.database.DBFunct;

import android.app.Application;
import android.widget.Toast;

public class TicTacTicTacToe extends Application{
	@Override
	public void onCreate(){
		super.onCreate();
		
		if(!DBFunct.initDB(getApplicationContext())){
			// Let the uer know something went wrong
			Toast.makeText(this, "An error has occured while initializing the database connection...", 
					Toast.LENGTH_SHORT).show();
		}
	}
}
