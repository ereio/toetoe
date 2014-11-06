package com.ToeTactics.tictactictactoe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphUser;

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
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	public final static String USER = "UserKey";
	public final static String FRIENDS = "FriendListKey";
	String APP_ID;
	Facebook fb;
	Button fbButton;
	AsyncFacebookRunner mAsyncRunner;
	
	Button bLogin;
	EditText eUsername;
	EditText ePassword;
	String usr; // stores facebook name 
	
	String nameEntry;
	String passEntry;
	String friendsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
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

	    } catch (NoSuchAlgorithmException e) {

	    }

		fbButton = (Button)findViewById(R.id.authButton);
		fbButton.setOnClickListener(this);
		
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
	}
	
	private boolean Auth(){
		return true;
	}
	
	
	private void StartGame(){
		Bundle extras = new Bundle();
		if(nameEntry != null && !nameEntry.equals("")){
			extras.putString(USER, nameEntry);
		}
		if(friendsList != null && !friendsList.equals("")){
			extras.putString(FRIENDS, friendsList);
		}
		//extras.putString("access_token", access_token);
		Intent success = new Intent(getApplicationContext(), GameBoard.class);
		success.putExtras(extras);
		startActivity(success);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (fb.isSessionValid())
		{
			try {
				fb.logout(getApplicationContext());
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		else // login
		{
			fb.authorize(this, new String[] {"user_friends", "public_profile"}, new DialogListener() {

				@Override
				public void onFacebookError(FacebookError e) {
					 Toast.makeText(MainActivity.this, "Authorization failed", Toast.LENGTH_SHORT).show();
					 Log.e("MainActivity", e.toString());
				}
				
				@Override
				public void onError(DialogError e) {
					 Toast.makeText(MainActivity.this, "Authorization failed", Toast.LENGTH_SHORT).show();
					 Log.e("MainActivity", e.toString());
				}
				
				@Override
				public void onComplete(Bundle values) {
					Toast.makeText(MainActivity.this, "Authorization successful", Toast.LENGTH_SHORT).show();
					
					getFacebookName();
					//getFacebookFriends();
					//StartGame();

				}
				
				@Override
				public void onCancel() {
					
					 //Toast.makeText(MainActivity.this, "Authorization failed", Toast.LENGTH_SHORT).show();
				}
			});
		}	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		fb.authorizeCallback(requestCode, resultCode, data);
	}
	
	@SuppressWarnings("deprecation")
	public void getFacebookName(){
		
		mAsyncRunner.request("me", new RequestListener(){

			@Override
			public void onComplete(String response, Object state) {
				//Log.i("MainActivity", response);
				
				try {
					JSONObject JSONresponse = new JSONObject(response);
					nameEntry = JSONresponse.getString("name");
					Log.i("MainActivity", nameEntry);
					getFacebookFriends();
					
				} catch (JSONException e) {
					Log.e("MainActivity",e.toString());
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
				Log.e("MainActivity",e.toString());
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				Log.e("MainActivity",e.toString());
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				Log.e("MainActivity",e.toString());
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				Log.e("MainActivity",e.toString());
			}
			
		});
		
	}
	
	@SuppressWarnings("deprecation")
	public void getFacebookFriends(){
		mAsyncRunner.request("me/friends", new RequestListener(){

			@Override
			public void onComplete(String response, Object state) {
				//Log.i("MainActivity", response);
				try{
					JSONObject JSONresponse = new JSONObject(response);
					friendsList = JSONresponse.getJSONArray("data").toString();
					Log.i("MainActivity",friendsList);
					StartGame();
					
				} catch(Exception e){
					Log.e("MainActivity",e.toString());
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
				Log.e("MainActivity",e.toString());
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				Log.e("MainActivity",e.toString());
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				Log.e("MainActivity",e.toString());
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
				Log.e("MainActivity",e.toString());
			}
			
		});
		
	}
}