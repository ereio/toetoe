package com.example.chatapplication;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ClientProcessor extends Thread{
	
	private SocketChannel channel;
	private List<byte[]> list;
	private LinearLayout layout;
	
	ArrayList<String> messages = new ArrayList<String>();
	
	public ClientProcessor(SocketChannel c, LinearLayout l){
		channel = c;
		layout = l;
		list = new LinkedList<byte[]>();
	}

	public void addData(byte[] data){
		list.add(data);
	}
	
	public void run(){
		while(true){
				
			while(!list.isEmpty()){
				processData();
				list.remove(0);
			}
			
		}
	}
	
	private void processData(){
		byte[] buffer = list.get(0);
		//System.out.println("Processed:" + new String(buffer));
		String m = new String(buffer);
		messages.add(m);
		displayMessages();
	}
	
	private void displayMessages(){
		//Handler handle = new Handler(context.getMainLooper());
		layout.post(new Runnable(){

			@Override
			public void run() {
				TextView t = null;
				while( !messages.isEmpty() ){
					t = new TextView(layout.getContext());
					t.setText(messages.get(0));
					messages.remove(0);
					layout.addView(t);
				}
			}
		});			
	}
	
	public void startDataProcessing(){
		if( !list.isEmpty() )
			synchronized(list){
				list.notify();
			}
	}
	
	
}