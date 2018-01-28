package com.ToeTactics.toetoe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import com.ToeTactics.toetoe.database.DBFunct;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements OnClickListener {
	//log tag
	public final static String TAG = "MainActivity";
	
	//bundle keys
	public final static String USER = "UserKey";
	public final static String FRIENDS = "FriendListKey";
	public final static String USERIDKEY = "UserIdKey";
	
	//fb vars
	String APP_ID;
	Facebook fb;
	Button fbButton;
	AsyncFacebookRunner mAsyncRunner;
	
	//login UI
	Button bLogin;
	Button bChangePassword;
	EditText eUsername;
	EditText ePassword; 
	
	//user data
	String nameEntry;
	String passEntry;
	String emailEntry;
	String friendsList;
	String userIDEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		/*
		if(!DBFunct.initDB(getApplicationContext())){
			// Let the uer know something went wrong
			Toast.makeText(this, "An error has occured while initializing the database connection...", 
					Toast.LENGTH_SHORT).show();
			finish();
		}
		*/
		
		// fb setup
		APP_ID = getString(R.string.app_id);
		fb = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(fb);

		try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.ToeTactics.tictactictactoe", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {
	    	Log.e(TAG,e.toString());
	    } catch (NoSuchAlgorithmException e) {
	    	Log.e(TAG,e.toString());
	    }

		fbButton = (Button)findViewById(R.id.authButton);
		fbButton.setOnClickListener(this);
		// end fb setup
		
		ui_init();
		
	}

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
		
		//int id = item.getItemId();
		
		return super.onOptionsItemSelected(item);
	}
	
	//--------------------------------------------------
	// User Interface Initalization
	//--------------------------------------------------
	private void ui_init(){
		bLogin = (Button) findViewById(R.id.LoginButton);
		bChangePassword = (Button) findViewById(R.id.ChangePassButton);
		eUsername = (EditText) findViewById(R.id.UsernameEntry);
		ePassword = (EditText) findViewById(R.id.PasswordEntry);
		
		//Saved for planned login option
		bLogin.setVisibility(View.GONE);
		bChangePassword.setVisibility(View.GONE);
		ePassword.setVisibility(View.GONE);
		eUsername.setVisibility(View.GONE);
		
		/*
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
		*/
	}
	/*
	private boolean Auth(){
		return true;
	}
	*/
	
	//--------------------------------------------------
	// Start GameBoard Activity
	//--------------------------------------------------
	private void StartGame(){
		Bundle extras = new Bundle();
		boolean name_set = false;
		boolean friends_set = false;
		boolean id_set = false;
		if(nameEntry != null && !nameEntry.equals("")){
			extras.putString(USER, nameEntry);
			name_set = true;
		}
		if(friendsList != null && !friendsList.equals("")){
			extras.putString(FRIENDS, friendsList);
			friends_set = true;
		}
		if(userIDEntry != null && !userIDEntry.equals("")){
			extras.putString(USERIDKEY, userIDEntry);
			id_set = true;
		}
		
		if(name_set && friends_set && id_set){
			Intent success = new Intent(getApplicationContext(), GameBoard.class);
			success.putExtras(extras);
			startActivity(success);
		}
		else{
			// Let the uer know something went wrong
			Toast.makeText(this, "An error has occured while logging in...", 
					Toast.LENGTH_SHORT).show();
		}
		
		finish();
	}

	//-----------------------------------------------
	// Facebook Button onClick
	//-----------------------------------------------
	@Override
	public void onClick(View v) {
		if (fb.isSessionValid())
		{
			try {
				fb.logout(getApplicationContext());
			} catch (MalformedURLException e) {
				Log.e(TAG,e.toString());
			} catch (IOException e) {
				Log.e(TAG,e.toString());
			}			
		}
		else // login
		{
			fb.authorize(this, new String[] {"user_friends", "public_profile", "email"}, new DialogListener() {

				@Override
				public void onFacebookError(FacebookError e) {
					 Toast.makeText(MainActivity.this, "Authorization failed", Toast.LENGTH_SHORT).show();
					 Log.e(TAG,e.toString());
				}
				
				@Override
				public void onError(DialogError e) {
					 Toast.makeText(MainActivity.this, "Authorization failed", Toast.LENGTH_SHORT).show();
					 Log.e(TAG,e.toString());
				}
				
				@Override
				public void onComplete(Bundle values) {
					getFacebookName();
				}
				
				@Override
				public void onCancel() {
				}
			});
		}	
	}
	
	//-----------------------------------------------------------------------------
	// Facebook Login Callback
	//-----------------------------------------------------------------------------
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		fb.authorizeCallback(requestCode, resultCode, data);
	}
	
	//----------------------------
	// Get FB Name From API
	//----------------------------
	public void getFacebookName(){
		
		mAsyncRunner.request("me", new RequestListener(){

			@Override
			public void onComplete(String response, Object state) {
				//Log.i("MainActivity", response);
				
				try {
					JSONObject JSONresponse = new JSONObject(response);
					nameEntry = JSONresponse.getString("name");
					userIDEntry = JSONresponse.getString("id");
					//temporarily using id a password
					passEntry = JSONresponse.getString("id");
					emailEntry = JSONresponse.getString("email");
					
					getFacebookFriends();
					
				} catch (JSONException e) {
					Log.e(TAG,e.toString());
					// TODO failed login alert
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
				Log.e(TAG,e.toString());
				// TODO failed login alert
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				Log.e(TAG,e.toString());
				// TODO failed login alert
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				Log.e(TAG,e.toString());
				// TODO failed login alert
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				Log.e(TAG,e.toString());
				// TODO failed login alert
			}
			
		});
		
	}
	
	//-----------------------------------------------------------
	// Get Friends List as JON Array from API
	//-----------------------------------------------------------
	public void getFacebookFriends(){
		mAsyncRunner.request("me/friends", new RequestListener(){

			@Override
			public void onComplete(String response, Object state) {
				//Log.i("MainActivity", response);
				try{
					JSONObject JSONresponse = new JSONObject(response);
					friendsList = JSONresponse.getJSONArray("data").toString();
					//Log.i("MainActivity",friendsList);
					if(!DBFunct.createUser(userIDEntry, nameEntry, passEntry, emailEntry)){
						if(!DBFunct.signIn(nameEntry, passEntry)){
							// TODO failed login alert
						}
						else{
							StartGame();
						}
					}
					else{
						if(DBFunct.getUser() != null){
							StartGame();
						}
					}
				} catch(Exception e){
					Log.e(TAG,e.toString());
					// TODO failed login alert
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
				Log.e(TAG,e.toString());
				// TODO failed login alert
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				Log.e(TAG,e.toString());
				// TODO failed login alert
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				Log.e(TAG,e.toString());
				// TODO failed login alert
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				Log.e(TAG,e.toString());
				// TODO failed login alert
			}
			
		});
		
	}
}