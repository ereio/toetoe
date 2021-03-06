package com.ToeTactics.toetoe;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ToeTactics.toetoe.database.DBFunct;
import com.ToeTactics.toetoe.database.TGame;
import com.ToeTactics.toetoe.database.TPlayer;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class GameBoard extends ActionBarActivity{
	// Push Notification JSON Key
	public static final String PUSH_KEY = "com.parse.Data";
	// JSON keys
	public static final String BOARDKEY = "BoardKey";
	
	// Static self ref
	public static GameBoard thisActivity = null;
	
	// no game error identifier
	public static final String NOGAME = "NoGame";
	
	// Log tag
	public static final String TAG = "GameBoard";
	
	// ID for local game
	public static final String LOCAL_GAME = "Local Game";
	
	// view handles
	private DrawerLayout mDrawerLayout;
	private Fragment fChat;
	private Fragment fBoard;
	private ListView playerList;
	private FrameLayout mChat;
	//private FrameLayout mBoard;
	private ActionBarDrawerToggle mDrawerToggle;
	
	// NIO vars
	//private ToeClient client;
	
	// user vars
	private String[] players = {LOCAL_GAME};
	private String[] player_ids = {LOCAL_GAME};
	private String username = "";
	public String userID = "";
	
	// current game object
	public TGame current_game;
	
	// receiver
	//private ParsePushBroadcastReceiver pushReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_game);
		
		InitUserVars();
		
		InitGame();
		
		// Init UI Components and Links with client
		ui_init();
		
		// Starts client
		//client = new ToeClient(this);
		//Thread t = new Thread(client);
		//t.start();

		if(savedInstanceState != null){
		//	SwitchPlayerBoard(NOGAME);
		}
		
		// Associate current user with installation (device)
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put("user", ParseUser.getCurrentUser());
		try {
			installation.save();
		} catch (ParseException e) {
			Log.e(TAG,e.toString());
		}
		
		// Set self ref for push receiver
		thisActivity = this;
		
		// Define receiver
/*		pushReceiver = new ParsePushBroadcastReceiver(){
			@Override
			public void onReceive(Context c, Intent i){
				receivePushMsg(i);
			}
		};
		
		// Set up intent filter
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.parse.push.intent.RECEIVE");
		filter.addAction("com.parse.push.intent.DELETE");
		filter.addAction("com.parse.push.intent.OPEN");
		
		// Register
		registerReceiver(pushReceiver,filter);
*/
	}
	
	//----------------------------
	// Process message from push 
	//----------------------------
	public void receivePushMsg(Intent i){
		// JSON Fetch and conversion
		Bundle bundle = i.getExtras();
		String jData = bundle.getString(PUSH_KEY);
		JSONObject jObject;
		try {
			jObject = new JSONObject(jData);
			jData = jObject.getString("data");
		} catch (JSONException e) {
			Log.e(TAG,e.toString());
			return;
		}
		
		// Get data from Push message jData conversion
		String[] data = jData.split("\t");
		
		if(data[0].equals("board")){
			// Data format: board id1 id2 x,x,x,x
			
			// Get player ids from push message
			String p1_fb_id = data[1];
			String p2_fb_id = data[2];
			// Get move coordinates from message
			String[] move = data[3].split(",");
		
			if(current_game.player1.facebook_id.equals(p1_fb_id) 
				&& current_game.player2.facebook_id.equals(p2_fb_id)){
				// Update the board
				Fragment boardFrag = getSupportFragmentManager()
						.findFragmentById(R.id.game_display);
				
				if(boardFrag instanceof GameBoardFragment){
					((GameBoardFragment) boardFrag)
						.receiveMove(Integer.parseInt(move[0]), 
								Integer.parseInt(move[1]), 
								Integer.parseInt(move[2]), 
								Integer.parseInt(move[3]));
				}
			}
		}
		if(data[0].equals("message")){
			// Data format: message sender_id "the message"
			
			// Check if message is for current game
			if(current_game.player1.facebook_id.equals(data[1]) 
					|| current_game.player2.facebook_id.equals(data[1])){
				// Update chat log
				Fragment chatFrag = getSupportFragmentManager()
						.findFragmentById(R.id.right_chat);
			
				if(chatFrag instanceof ChatFragment){
					((ChatFragment) chatFrag).setMessage(data[2]);
				}
			}
		}
	}
	
	// On Destroy
	@Override
	public void onDestroy(){
		
		super.onDestroy();
		
		// Unset self ref
		thisActivity = null;
		
		// Unregister push receiver
		//unregisterReceiver(pushReceiver);
		
		// Sign user out
		DBFunct.signOut();
	}
	
	// On Resume - BroadCast receiver
	@Override
	public void onResume(){
		super.onResume();
		
	}
	// On Pause  - BroadCast Receiver
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		// Create new fragments to replace old ones
		Fragment fNewBoard = new GameBoardFragment();
		Fragment fNewChat = new ChatFragment();
		
		// Replace fragments
		FragmentManager fManager = getSupportFragmentManager();
		FragmentTransaction fTrans = fManager.beginTransaction();
		// Replace GameBoardFragment instance
		fTrans.replace(R.id.game_display, fNewBoard);
		// Replace ChatFragment Instance
		fTrans.replace(R.id.right_chat, fNewChat);
		fTrans.commit();
	}
	

	//----------------------------------------------------------------------------
	// Initialize user variables
	//----------------------------------------------------------------------------
	private void InitUserVars(){
		if(getIntent().getExtras() != null){
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
				
					// Put player names in an ArrayList
					ArrayList<String> tempPlayers = new ArrayList<String> ();
					for(int i = 0; i < friendListObj.getJSONArray("data").length(); i++){
						//Log.i(TAG,friendListObj.getJSONArray("data")
						//						.getJSONObject(i).toString());
						
						tempPlayers.add(friendListObj.getJSONArray("data")
													 .getJSONObject(i)
													 .getString("name"));
					}
					
					// Resize players array
					players = new String[tempPlayers.size()+1];
					// Populate players array with player names
					for(int i = 1; i < players.length; i++){
						players[i] = (String) tempPlayers.get(i-1);
					}
					
					// Resize player_ids array
					player_ids = new String[players.length];
					// Populate player_ids array with fb ids
					for(int i = 1; i < player_ids.length; i++){
						player_ids[i] = friendListObj.getJSONArray("data")
													 .getJSONObject(i-1)
													 .getString("id");
					}
					
					// Set local game option
					players[0] = LOCAL_GAME;
					player_ids[0] = LOCAL_GAME;
				}
			} catch (JSONException e) {
				Log.e(TAG,e.toString());
			}
		}
	}
	
	//-------------------------------------------------------------------------
	// Creates local game from onCreate
	//-------------------------------------------------------------------------
	private void InitGame(){
		try {
			current_game = new TGame(LOCAL_GAME);
			current_game.player1 = DBFunct.getUser();
			current_game.player2 = DBFunct.getUser();
			current_game.current_player_id = current_game.player1.facebook_id;
			current_game.board = new JSONArray(DBFunct.EMPTY_JSON_BOARD);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			// Let the user know something went wrong
			Toast.makeText(this, "An error has occured while initializing the game...",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	//-------------------------------------------------------------------------
	// Player List onClick
	//-------------------------------------------------------------------------
	private class PlayerClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				SwitchPlayerBoard(position);
				playerList.setItemChecked(position, true);
				// update a text view of the current player on screen
				mDrawerLayout.closeDrawers();
		}
	}
	
	//-------------------------------------------------------------------------
	// Change Opponent
	//-------------------------------------------------------------------------
	private void SwitchPlayerBoard(int player){
		// Set game info
		if(players[player] != NOGAME && players[player] != LOCAL_GAME){
			// Get game from database
			current_game = 
				DBFunct.startGame(new TPlayer(player_ids[player], players[player]));
			
		}
		else{
			// Start local game
			InitGame();
		}
		
		// Create new fragments to replace old ones
		Fragment fNewBoard = new GameBoardFragment();
		Fragment fNewChat = new ChatFragment();

		// Replace fragments
		FragmentManager fManager = getSupportFragmentManager();
		FragmentTransaction fTrans = fManager.beginTransaction();
		// Replace GameBoardFragment instance
		fTrans.replace(R.id.game_display, fNewBoard);
		// Replace ChatFragment Instance
		fTrans.replace(R.id.right_chat, fNewChat);
		fTrans.commit();
	}
	
	//--------------------------------------------------
	// Initalization of the main UI data and processes
	//--------------------------------------------------
	private void ui_init(){
		frag_init();
		
		// Get handle on drawers
		mChat = (FrameLayout) findViewById(R.id.right_chat);
		//mBoard = (FrameLayout) findViewById(R.id.game_display);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		playerList = (ListView) findViewById(R.id.left_nav);
		
		// Stylize drawers
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		// Try the Drawer Toggle Settings Creation
		try{	
			mDrawerToggle = 
				new CustomDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Closed);

		}catch(RuntimeException e) {
			Log.e(TAG, e.toString());
		}
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		//Sets ActionBar App Icon to essentially clear opened drawers
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		// Creates player list array // WHERE PLAYERS ARE SET
		playerList.setAdapter(new ArrayAdapter<String>(this, R.layout.player_entry, players));
		playerList.setOnItemClickListener(new PlayerClickListener());
		
	}
	
	//-----------------------------------------------------------
	// Initialize 
	//-----------------------------------------------------------
	private void frag_init(){
		fBoard = new GameBoardFragment();
		fChat = new ChatFragment();
		FragmentManager fManager = getSupportFragmentManager();
		FragmentTransaction fTrans = fManager.beginTransaction();
		fTrans.add(R.id.game_display, fBoard);
		fTrans.add(R.id.right_chat, fChat);
		fTrans.commit();
	}

	// --------------------------------------------------------
	// FUNCTIONS FOR CLIENT / ACTIVTY NIO
	/*
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
	*/
	// END FUNCTIONS FOR CLIENT / ACTIVITY NIO
	// --------------------------------------------------------
	
	//-----------------------------------------------------
	// Used to sync the Drawer state 
	//-----------------------------------------------------
	@Override
	protected void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	//----------------------------------------------------------
	// Used to pass config changes to Drawer Toggle
	//----------------------------------------------------------
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	//----------------------------------------------------------
	// Creates menu from layout
	//----------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//----------------------------------------------------------
	// Checks options and if something is open already
	//----------------------------------------------------------
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		boolean isPlayerOpen = mDrawerLayout.isDrawerOpen(playerList);
		boolean isChatOpen = mDrawerLayout.isDrawerOpen(mChat);
		if(isPlayerOpen || isChatOpen){
			menu.clear();
		}
		return super.onPrepareOptionsMenu(menu);
	}

	//----------------------------------------------------------
	// On Options Selected for action bar
	//----------------------------------------------------------
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
        	// Pass the event to ActionBarDrawerToggle, if it returns
        	// true, then it has handled the app icon touch event
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
			
			/*if (id == R.id.action_settings) {
				Intent settings = new Intent(getApplicationContext(), Settings.class);
				settings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(settings);
				return true;
			} else*/ 
		
		
		if (id == R.id.action_logout) {
			// Unset auto sign in flag
			//sharedprefs code
			
			// Restart login activity
			Intent logout = new Intent(getApplicationContext(), MainActivity.class);
			logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(logout);
			finish();
			
			return true;
			
		} else if(id == R.id.action_chat){
			mDrawerLayout.openDrawer(mChat);
		    return true;
		     
		} else{
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	//--------------------------------------------------------------
	// Class for creation of toggle for Drawer events
	//--------------------------------------------------------------
	private class CustomDrawerToggle extends ActionBarDrawerToggle {

		public CustomDrawerToggle(Activity activity, DrawerLayout drawerLayout,
				int openDrawerContentDescRes, int closeDrawerContentDescRes) {
			super(activity, drawerLayout, openDrawerContentDescRes,
					closeDrawerContentDescRes);
		}

		@Override
		public void onDrawerOpened(View drawerView){
			boolean isPlayerOpened = mDrawerLayout.isDrawerOpen(playerList);
			boolean isChatOpened = mDrawerLayout.isDrawerOpen(mChat);
			if(isChatOpened){
				getSupportActionBar().setTitle(username + " " + getString(R.string.ChatPrompt));
				mDrawerLayout.closeDrawer(playerList);
			}
			if(isPlayerOpened){
				getSupportActionBar().setTitle(getString(R.string.PlayersPrompt));
				mDrawerLayout.closeDrawer(mChat);
			}
			supportInvalidateOptionsMenu();
			mDrawerToggle.syncState();
		}
		
		@Override
		public void onDrawerClosed(View drawerView){
			getSupportActionBar().setTitle(getString(R.string.app_name));
			supportInvalidateOptionsMenu();
			mDrawerToggle.syncState();
		}
	}
}
