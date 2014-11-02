package com.ToeTactics.tictactictactoe;

import android.app.Fragment;
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

public class ChatFragment extends Fragment{
	
	Button send;
	TextView text;
	LinearLayout layout;
	EditText message;
	
	Client client = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							Bundle savedInstanceState ){
		
		/*client = new Client(layout, getView());
		Thread t = new Thread(client);
		t.start();*/
		
		return inflater.inflate(R.layout.chat_pane, container, false);
	}
	
	public ChatFragment(){
	}
	
	public void init(){
		send = (Button) getView().findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String m = message.getText().toString();
				client.write(m);
				
			}
		});
		message = (EditText) getView().findViewById(R.id.message);
		layout = (LinearLayout) getView().findViewById(R.id.container);
	}
}
