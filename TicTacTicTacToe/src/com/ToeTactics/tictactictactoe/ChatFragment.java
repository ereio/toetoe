package com.ToeTactics.tictactictactoe;

import org.json.JSONArray;

import com.ToeTactics.tictactictactoe.database.DBFunct;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChatFragment extends Fragment{
	// Log tag
	public static final String TAG = "ChatFragment";
	
	// Activity handle
	GameBoard gbActivity;
	
	// Views
	Button send;
	TextView text;
	LinearLayout layout;
	EditText message;
	
	// Context for ???
	Context fContext = null;
	
	// username for ???
	String userName = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							Bundle savedInstanceState ){
		return inflater.inflate(R.layout.chat_pane, container, false);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		init();
		
	}
	
	//-------------------------------------------------------
	// Initialize the fragment UI
	//-------------------------------------------------------
	public void init(){
		userName = "Testing";
		
		gbActivity = (GameBoard) getActivity();

		// Get handle on views
		message = (EditText) getView().findViewById(R.id.message);
		layout = (LinearLayout) getView().findViewById(R.id.container);
		send = (Button) getView().findViewById(R.id.send);

		// Set onClick
		send.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				//String m = message.getText().toString();
				//((GameBoard)getActivity()).sendMessage(m);
				
				if(!message.getText().toString().equals("")){
					// Get name and message
					String m = DBFunct.getUser().name + ": " + message.getText().toString();
				
					// Send message if not local game
					if(gbActivity.current_game != null && 
							!gbActivity.current_game.obj_id.equals(GameBoard.LOCAL_GAME)){
						// Set game chat log and update in db
						if(gbActivity.current_game.chat_log == null){
							gbActivity.current_game.chat_log = new JSONArray();
						}
						gbActivity.current_game.chat_log.put(m);
						DBFunct.updateGame(gbActivity.current_game);
					
						// Send push with the message
						DBFunct.sendMessagePush(gbActivity.current_game, m);
					
						// Update the UI
						setMessage(m);
					}
					else{
						// Update chat log; don't send push or save in db
						setMessage(message.getText().toString());
					}
				
					// Clear text view and close keyboard
					message.setText("");
				}
			}
		});
		
		// Initialize the chat log
		initLog();
	}
	
	//----------------------------------------------
	// Initialize the chat log for the current game
	//----------------------------------------------
	public void initLog(){
		// Get chat log from game
		if(gbActivity.current_game.chat_log == null){
			gbActivity.current_game.chat_log = new JSONArray();
		}
		JSONArray log = gbActivity.current_game.chat_log;
		
		// Update the UI
		try{
			for(int i = 0; i < log.length(); i++){
				setMessage(log.getString(i));
			}
		} catch(Exception e){
			Log.e(TAG,e.toString());
			Toast.makeText(getActivity(), 
					"Failed to initialize the chat log...", 
					Toast.LENGTH_LONG).show();
		}
	}

	//-------------------------------------------
	// Add a message to the log
	//-------------------------------------------
	public void setMessage(String message){
		TextView t = new TextView(getActivity());
		message.trim();
		
		if(!message.isEmpty()){
			t.setText(message);
			layout.addView(t);
		}
		
	}
}
