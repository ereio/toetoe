package com.ToeTactics.tictactictactoe;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatFragment extends Fragment{
	// Log tag
	public static final String TAG = "ChatFragment";
	
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
			}
		});
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
