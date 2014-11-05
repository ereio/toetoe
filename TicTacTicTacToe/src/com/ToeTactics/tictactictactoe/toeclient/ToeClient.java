package com.ToeTactics.tictactictactoe.toeclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ToeTactics.tictactictactoe.GameBoard;

import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToeClient implements Runnable {
	public static final String TAG = "ToeChatClient: ";
	
	SocketChannel socketChannel = null;
	Selector selector = null;
	
	String dataString;
	
	ByteBuffer buffer = ByteBuffer.allocate(1024);
	LinearLayout layout;
	
	Activity activity;
	int openPort = 40052;
	byte[] temp = new byte[1000];
	
	Map list = new HashMap<SelectionKey, byte[]>();

	
	public ToeClient(Activity a){
		activity = a;
	}
	
	@Override
	public void run() {
		init_Connection();
		
		while(true){
        	
			try {
				selector.select();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
			Iterator i = selector.selectedKeys().iterator();
			
			while(i.hasNext()){
				SelectionKey key = (SelectionKey) i.next();
				i.remove();
				
				if(key.isConnectable()){
					prep_connection(key);
				} else if(key.isReadable()){
					String result = checkOperation(key);
					
					if(result == Ops.MESSAGE){
						incomingMessage(key);
					} else if(result == Ops.BOARD){
						incomingBoard(key);
					} else if(result == Ops.MESSAGE){
						incomingPlayerRequest(key);
					} else if(result == Ops.INVALID){
						
					}
				} else if(key.isWritable()){
					
				}
			
			
			
			
			}
		}
	}
	
	// may need an update layout function
	
	private void init_Connection(){
		try{
	        socketChannel = SocketChannel.open();
	        socketChannel.configureBlocking(false);
	     //   try{
	    //    	socketChannel.connect(new InetSocketAddress("bearnet.ddns.net", openPort));
	       
	    //    } catch (ConnectionPendingException e){
	        	socketChannel.connect(new InetSocketAddress("192.168.0.18", openPort));
	    //    }
	        SelectorProvider provider = SelectorProvider.provider();
	        Log.i(TAG, provider.toString());
	        selector = SelectorProvider.provider().openSelector();
	        
	        socketChannel.register(selector, SelectionKey.OP_CONNECT);
	        
    	}catch(IOException e){
    		Log.i(TAG, "Client Failed To Establish with Host");
    		e.printStackTrace();
    	}
	}
	
	private void prep_connection(SelectionKey key){
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
	}
	
	
	public void outgoingMessage(String message, String userName)
	{	final String m = Ops.MESSAGE + userName + " : " + message;
		
		Thread thread = new Thread()
		{
			@Override
			public void run(){
				buffer.clear();
				buffer = ByteBuffer.wrap(m.getBytes());
		
				if(buffer != null){
					buffer.order(ByteOrder.BIG_ENDIAN);
					System.out.println( "BYTES: " + new String(buffer.array()));
					
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
				
				Log.i(TAG, "outgoingMessage Finished");
			}
		};
		thread.start();
	}
	
	public void outgoingBoard(String JSON_Board){
		final String b = Ops.BOARD + JSON_Board;
		Thread thread = new Thread(){
			@Override
			public void run(){
			
			buffer.clear();	
			buffer = ByteBuffer.wrap(b.getBytes());
			
			if(buffer != null){
				buffer.order(ByteOrder.BIG_ENDIAN);
				System.out.println(TAG + " OUTGOING B:" + new String (buffer.array()));
				
				try{
					int byteWritten = socketChannel.write(buffer);
					
					if(byteWritten != -1){
						socketChannel.register(selector, SelectionKey.OP_READ);
					}
				} catch (IOException e) { 
				   e.printStackTrace(); }
				
			}
			}
		};
	}
	
	public void outgoingPlayerRequest(String curUsername, String friendUsername){
		
		
	}
	
	public void incomingBoard(SelectionKey key){
		try {
			SocketChannel channel = (SocketChannel)key.channel();
			int bytesRead;
			
			while( (bytesRead = channel.read(buffer)) != 0){							 
				String opBoard = convertByteBufferToString(buffer, bytesRead);
				final String rBoard = parseOpFromString(opBoard);
				
				if(bytesRead == -1 ){
					
				}else{
					this.activity.runOnUiThread(new Runnable(){
						@Override
						public void run() {
						((GameBoard) activity).receiveBoard(rBoard);
						}
					});
				}
			}						
			buffer.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void incomingMessage(SelectionKey key){
		try {
			SocketChannel channel = (SocketChannel)key.channel();
			int bytesRead;
			
			while( (bytesRead = channel.read(buffer)) != 0){							 
				String opMessage = convertByteBufferToString(buffer, bytesRead);
				final String rMessage = parseOpFromString(opMessage);
				
				if(bytesRead == -1 ){
					
				}else{
					this.activity.runOnUiThread(new Runnable(){
						@Override
						public void run() {
						((GameBoard) activity).receiveMessage(rMessage);
						}
					});
				}
			}						
			buffer.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void incomingPlayerRequest(SelectionKey key){
		
	}
	
	
	
	public String checkOperation(SelectionKey key){
		try{
		
		SocketChannel channel = (SocketChannel) key.channel();
		int numBytes = 0;
		// PROBLEM, NO BITES IN THE CHANNELS BUFFER
		numBytes = channel.read(buffer);
		while( (numBytes = channel.read(buffer)) != 0){
			String bData = convertByteBufferToString(buffer, numBytes);
			return findOpsFromString(bData);
		}
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return Ops.INVALID;
	}
	
	public String parseOpFromString(String bData){
		int opIndex = bData.indexOf(":");
		String realData = bData.substring(opIndex+1, bData.length()-1);
		return realData;

	}
	
	public String findOpsFromString(String bData){
		int opIndex = bData.indexOf(":");
		String foundCode = bData.substring(0,opIndex);
		return foundCode;
	}
	
	private void DEBUG_readBytes(ByteBuffer bb, int bSize){
		ByteBuffer byteMessage = trimByteBuffer(bb, bSize);
		byte[] bArray = byteMessage.array();
		
		// Creates a String from the byte array created from a bytebuffer
		String message = new String(bArray);
		
		System.out.println("ORGINAL BB" + bb);
		System.out.println("TEMP BB:" + byteMessage);
		System.out.println("bArray to String: " + message);
		System.out.println("MainBuffer size: " + bSize);
		System.out.println("byteMessage size: " + byteMessage.capacity());
	}

	private ByteBuffer trimByteBuffer(ByteBuffer bb, int bSize){
		ByteBuffer temp = ByteBuffer.allocate(bSize);
		
		bb.rewind();
		for(int b = 0; b < bSize; b++){
			temp.put(bb.get());
			}
		
		return temp;
	}
	
	private String convertByteBufferToString(ByteBuffer bb, int bSize){
		ByteBuffer temp = ByteBuffer.allocate(bSize);
		
		bb.rewind();
		for(int b = 0; b < bSize; b++){
			temp.put(bb.get());
			}
		
		byte[] bArray = temp.array();
		return new String(bArray);
	}
}
