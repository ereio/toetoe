package com.ToeTactics.tictactictactoe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import com.ToeTactics.tictactictactoe.database.DBFunct;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
//import com.facebook.model.GraphUser;

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
//import android.widget.ImageView;
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
	EditText eUsername;
	EditText ePassword;
	//String usr; // stores facebook name 
	
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
		if(!DBFunct.initDB(getApplicationContext())){
			// TODO db connection failed alert
			finish();
		}
		
		//fb setup
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
		//end fb setup
		
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//--------------------------------------------------
	// User Interface Initalization
	//--------------------------------------------------
	private void ui_init(){
		bLogin = (Button) findViewById(R.id.LoginButton);
		eUsername = (EditText) findViewById(R.id.UsernameEntry);
		ePassword = (EditText) findViewById(R.id.PasswordEntry);
		
		//Saved for planned login option
		bLogin.setVisibility(View.GONE);
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
		if(nameEntry != null && !nameEntry.equals("")){
			extras.putString(USER, nameEntry);
		}
		if(friendsList != null && !friendsList.equals("")){
			extras.putString(FRIENDS, friendsList);
		}
		if(userIDEntry != null && !userIDEntry.equals("")){
			extras.putString(USERIDKEY, userIDEntry);
		}
		Intent success = new Intent(getApplicationContext(), GameBoard.class);
		success.putExtras(extras);
		startActivity(success);
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
				Log.i("MainActivity", response);
				
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
					Log.i("MainActivity",friendsList);
					if(!DBFunct.createUser(userIDEntry, nameEntry, passEntry, emailEntry)){
						if(!DBFunct.signIn(nameEntry, passEntry)){
							// TODO failed login alert
						}
						else{
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