package com.ToeTactics.tictactictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	public final static String USER = "UserKey";
	public final static String PASS = "PassKey";
	
	ImageButton bTwitter;
	Button bLogin;
	EditText eUsername;
	EditText ePassword;
	
	String nameEntry;
	String passEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		ui_init();
		
	}

	
	
	// User Interface Initalization
	//--------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void ui_init(){
		bLogin = (Button) findViewById(R.id.LoginButton);
		bTwitter = (ImageButton) findViewById(R.id.twitterLogin);
		eUsername = (EditText) findViewById(R.id.UsernameEntry);
		ePassword = (EditText) findViewById(R.id.PasswordEntry);
		
		nameEntry = new String();
		passEntry = new String();
		
		bLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nameEntry = eUsername.getText().toString();
				passEntry = ePassword.getText().toString();
				if(Auth()){
					StartGame();
				}
			}
		});
		
		bTwitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nameEntry = eUsername.getText().toString();
				passEntry = ePassword.getText().toString();
				if(TwitterAuth()){
					StartGame();
				}
			}
		});
	}
	
	private boolean Auth(){
		return true;
	}
	
	private boolean TwitterAuth(){
		return true;
	}
	
	private void StartGame(){
		Bundle extras = new Bundle();
		extras.putString(USER, nameEntry);
		extras.putString(PASS, passEntry);
		Intent success = new Intent(getApplicationContext(), GameBoard.class);
		success.putExtras(extras);
		startActivity(success);
	}
}
