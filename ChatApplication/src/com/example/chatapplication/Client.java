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
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Client implements Runnable{
	
	SocketChannel socketChannel = null;
	
	Selector selector = null;
	
	ClientProcessor processor;
	
	String message;
    
    ByteBuffer buffer = ByteBuffer.allocate(124);
    
    LinearLayout layout;
    Activity activity;
    
    byte [] temp = new byte[1000];

	Map list = new HashMap<SelectionKey, byte[]>();
	
	public Client(LinearLayout l, Activity a){
		
		layout = l;
		activity = a;
		

		
	}

	
	public void run() {
		try{
	        socketChannel = SocketChannel.open();
	        socketChannel.configureBlocking(false);
	        socketChannel.connect(new InetSocketAddress("192.168.0.18", 40052));
	        
	        //SelectorProvider provider = SelectorProvider.provider();
	        selector = SelectorProvider.provider().openSelector();
	        
	        socketChannel.register(selector, SelectionKey.OP_CONNECT);
	        
    	}catch(IOException e){
    		e.printStackTrace();
    	}
        while( true ){
        	
        	//processor.startDataProcessing();
        
        	try {
				selector.select();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        	Iterator i = selector.selectedKeys().iterator();
        	
        	while(i.hasNext()){
        		
        		SelectionKey key = (SelectionKey)i.next();
        		i.remove();
        		
        		if(key.isConnectable()){
        			
        				try {
							if(socketChannel.finishConnect()){
								key.interestOps(SelectionKey.OP_READ);
								System.out.println("Connected!");
							}else{
								
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							key.cancel();
	        				Log.d("Client", e.getCause().toString());
						}
        		}else if( key.isReadable() ){
        			
        			SocketChannel channel = (SocketChannel)key.channel();
        			
        			try {
        				
						int bytesRead;
						
						while( (bytesRead = channel.read(buffer)) != 0){							 
							
							if(bytesRead == -1){
								
							}else{
								
								this.activity.runOnUiThread(new Runnable(){
									@Override
									public void run() {
										TextView t = new TextView(activity);
										
										byte[]array = buffer.array();
										
										String message = new String(array);
										
										message.trim();
										
										if(!message.isEmpty()){
											t.setText(message);
											layout.addView(t);
										}
									}
								});
							}
						}						

						buffer.clear();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			
        		}else if(key.isWritable()){
        			
        			
        			
        			
        		}
        	}//End Of Iterator Loop     	    
        }//End of Main Run Loop
	}//End of Run Method
	
	public void write(String rMessage){
		final String message = rMessage;
		Thread thread = new Thread(){
			@Override
			public void run(){
		buffer.clear();
		
		buffer = ByteBuffer.wrap(message.getBytes());
		
		if(buffer != null){
			buffer.order(ByteOrder.BIG_ENDIAN);
			//System.out.println( "MESSAGE: " + new String(buffer.array()));
			try {
				int bytesWritten = socketChannel.write(buffer);
				
				if(bytesWritten != -1){
					socketChannel.register(selector, SelectionKey.OP_READ);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("MESSAGE IS NULL!");
		}
			}
		};
		thread.start();
	}
	
}