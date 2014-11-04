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

import com.ToeTactics.tictactictactoe.DBClient.Client;
import com.ToeTactics.tictactictactoe.toeclient.ToeClient;

public class ChatFragment extends Fragment{
	
	Button send;
	TextView text;
	LinearLayout layout;
	EditText message;
	
	// Client client = null;
	ToeClient client = null;
	Context fContext = null;
	String userName = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							Bundle savedInstanceState ){
		return inflater.inflate(R.layout.chat_pane, container, false);
	}
	
	@Override
	public void onStart(){
		init();
//		client = new Client(layout, getActivity());
//		Thread t = new Thread(client);
//		t.start();
		super.onStart();
	}
	
	public void init(){
		userName = "Testing";
		send = (Button) getView().findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				String m = message.getText().toString();
				client.writeMessage(m,userName);
				
			}
		});
		message = (EditText) getView().findViewById(R.id.message);
		layout = (LinearLayout) getView().findViewById(R.id.container);
	}
	
	public void setClient(ToeClient c){
		client = c;
	}
}
