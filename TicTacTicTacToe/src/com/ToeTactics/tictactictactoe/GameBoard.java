package com.ToeTactics.tictactictactoe;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.ToeTactics.tictactictactoe.toeclient.ToeClient;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class GameBoard extends Activity{
	public static final String BOARDKEY = "BoardKey";
	public static final String NOGAME = "NoGame";
	public static final String TAG = "GameBoard";
	private DrawerLayout mDrawerLayout;
	private Fragment fChat;
	private Fragment fBoard;
	private ListView playerList;
	private FrameLayout mChat;
	private FrameLayout mBoard;
	private ToeClient client;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private String[] players = {"Joe", "Sally", "Bob"};
	private String username = "DefaultName";
	public String userID = "0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_game);
		
		if(getIntent().getExtras() != null){
			//Log.i(TAG, getIntent().getExtras().getString(MainActivity.FRIENDS));
			
			if(getIntent().getExtras().getString(MainActivity.USER) != null){
				username = getIntent().getExtras().getString(MainActivity.USER);
			}
			if(getIntent().getExtras().getString(MainActivity.USERIDKEY) != null){
				userID = getIntent().getExtras().getString(MainActivity.USERIDKEY);
			}
			try {
				if(getIntent().getExtras().getString(MainActivity.FRIENDS) != null){
					JSONObject friendListObj = new JSONObject("{\"data\":"+getIntent()
																.getExtras()
																.getString(MainActivity.FRIENDS)+"}");
				
		
					ArrayList<String> tempPlayers = new ArrayList<String> ();
		
					for(int i = 0; i < friendListObj.getJSONArray("data").length(); i++){
						/*Log.i(TAG,friendListObj.getJSONArray("data")
												.getJSONObject(i)
												.getString("name"));
						*/
						tempPlayers.add(friendListObj.getJSONArray("data")
													.getJSONObject(i)
													.getString("name"));
					}
					
					String[] tempPlayersArray = new String[tempPlayers.size()];
					for(int i = 0; i < tempPlayers.size(); i++){
						tempPlayersArray[i] = (String) tempPlayers.get(i);
					}
					
					players = tempPlayersArray;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Init UI Components and Links with client
		ui_init();
		
		// Starts client
		client = new ToeClient(this);
		Thread t = new Thread(client);
		t.start();

		if(savedInstanceState != null){
		//	SwitchPlayerBoard(NOGAME);
		}
		Log.i(TAG, "Passing onCreate");
	}
	
	
	// User Interface Initialization
	//--------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		boolean isPlayerOpen = mDrawerLayout.isDrawerOpen(playerList);
		boolean isChatOpen = mDrawerLayout.isDrawerOpen(mChat);
		if(isPlayerOpen || isChatOpen){
			menu.clear();
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.action_settings) {
			Intent settings = new Intent(getApplicationContext(), Settings.class);
			settings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(settings);
			return true;
		} else if (id == R.id.action_logout) {
			Intent logout = new Intent(getApplicationContext(), MainActivity.class);
			logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(logout);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class PlayerClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				SwitchPlayerBoard(players[position]);
				playerList.setItemChecked(position, true);
				// update a text view of the current player on screen
				mDrawerLayout.closeDrawers();
		}
	}
	
	private void SwitchPlayerBoard(String player){
		// must query for board moves related to a player
		// if none, it will need to create a new game instance
		
		// POTENTIALLY OBSOLTE, Might just need to call sendPlayerPlayRequest() 
		String JSON_board = "";
		
		if(player != NOGAME){
			// will rerender the correct board for the players involved
			Fragment fNewBoard = new GameBoardFragment();
			Bundle boardArgs = new Bundle();
			boardArgs.putString(BOARDKEY, JSON_board);
			
			FragmentManager fManager = getFragmentManager();
			FragmentTransaction fTrans = fManager.beginTransaction();
			fTrans.replace(R.id.game_display, fNewBoard);
			fTrans.commit();
		}
		else{
			JSON_board = "";
		}

		
	}
	
	/*
	// Initalization of the main UI data and processes
	*/
	private void ui_init(){
		
		frag_init();
		mChat = (FrameLayout) findViewById(R.id.right_chat);
		mBoard = (FrameLayout) findViewById(R.id.game_display);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		playerList = (ListView) findViewById(R.id.left_nav);
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		// Try the Drawer Toggle Settings Creation
		try{	
		mDrawerToggle = 
		new CustomDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Closed);
		
		}catch(RuntimeException e) {
			e.printStackTrace();
		}
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		//Sets ActionBar App Icon to essentially clear opened drawers
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		// Creates player list array // WHERE PLAYERS ARE SET
		playerList.setAdapter(new ArrayAdapter<String>(this, R.layout.player_entry, players));
		playerList.setOnItemClickListener(new PlayerClickListener());
		
	}
	
	private void frag_init(){
		fBoard = new GameBoardFragment();
		fChat = new ChatFragment();
		FragmentManager fManager = getFragmentManager();
		FragmentTransaction fTrans = fManager.beginTransaction();
		fTrans.add(R.id.game_display, fBoard);
		fTrans.add(R.id.right_chat, fChat);
		fTrans.commit();
	}

	// FUNCTIONS FOR CLIENT / ACTIVTY NIO
	// --------------------------------------------------------
	
	public void sendMessage(String message){
		client.outgoingMessage(message, username);
	}
	
	public void sendPlayerPlayRequest(String curUserame, String friendUsername){
		client.outgoingPlayerRequest(curUserame, friendUsername);
	}
	
	public void sendBoard(String JSON_Board){
		client.outgoingBoard(JSON_Board);
		
	}
	
	public void receiveMessage(String message){
		Fragment chatFrag = getFragmentManager().findFragmentById(R.id.right_chat);
		if(chatFrag instanceof ChatFragment)
		((ChatFragment) chatFrag).setMessage(message);
	}
	
	public void receiveBoard(String JSON_Board){
		Fragment boardFrag = getFragmentManager().findFragmentById(R.id.game_display);
		if(boardFrag instanceof GameBoardFragment){
			((GameBoardFragment) boardFrag).initBoard(JSON_Board);
		}
	}
	
	public void receivePlayerList(){
		
		
	}
	// END FUNCTIONS FOR CLIENT / ACTIVITY NIO
	// --------------------------------------------------------
	
	// Used to sync the Drawer state 
	@Override
	protected void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	// used to pass config changes to Drawer Toggle
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	/*
	// Class for creation of toggle for Drawer events
	*/
	private class CustomDrawerToggle extends ActionBarDrawerToggle {

		public CustomDrawerToggle(Activity activity, DrawerLayout drawerLayout,
				int openDrawerContentDescRes, int closeDrawerContentDescRes) {
			super(activity, drawerLayout, openDrawerContentDescRes,
					closeDrawerContentDescRes);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onDrawerOpened(View drawerView){
			boolean isPlayerOpened = mDrawerLayout.isDrawerOpen(playerList);
			boolean isChatOpened = mDrawerLayout.isDrawerOpen(mChat);
			if(isChatOpened){
				getActionBar().setTitle(username + " " + getString(R.string.ChatPrompt));
			}
			if(isPlayerOpened){
				getActionBar().setTitle(getString(R.string.PlayersPrompt));
			}
			invalidateOptionsMenu();
			mDrawerToggle.syncState();
		}
		
		@Override
		public void onDrawerClosed(View drawerView){
			getActionBar().setTitle(getString(R.string.app_name));
			invalidateOptionsMenu();
			mDrawerToggle.syncState();
		}
	}
}
