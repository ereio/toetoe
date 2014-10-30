package com.example.chatapplication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	Button send;
	TextView text;
	LinearLayout layout;
	EditText message;
	
	Client client = null;
	
	private String array[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		client = new Client(layout, this);
		Thread t = new Thread(client);
		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void init(){
		send = (Button)findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String m = message.getText().toString();
				client.write(m);
				
			}
		});
		message = (EditText)findViewById(R.id.message);
		layout = (LinearLayout)findViewById(R.id.container);
	}
}
