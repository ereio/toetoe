package com.ToeTactics.toetoe;

import org.json.JSONArray;

import com.ToeTactics.toetoe.GameBoardLogic.Board;
import com.ToeTactics.toetoe.database.DBFunct;


import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameBoardFragment extends Fragment {
	// Log tag
	public static final String TAG = "GameBoardFragment";
	
	// GameBoard activity handle
	public static GameBoard gbActivity;

	// Board of ImageViews
	public ImageView[][][][] spaces = new ImageView[3][3][3][3];
	public ImageView[][] sub_winners = new ImageView[3][3];
	
	// Tells the user it's their turn
	public TextView turn_tv;

	// Game board
	public Board board = new Board(); 

	//-----------------------------------------------------------------------------------
	// onCreateView
	//-----------------------------------------------------------------------------------
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state){
		return inflater.inflate(R.layout.game_board, container, false);
	}
	
	//-------------------------------------------------
	// Builds alert for when the game i over
	//-------------------------------------------------
	public AlertDialog buildEndGameAlert(String winner){
		AlertDialog.Builder ad_builder = new AlertDialog.Builder(getActivity());
		ad_builder.setMessage("Play again?")
				  .setTitle(winner+" wins!")
				  .setPositiveButton("Sure, why not", new DialogInterface.OnClickListener() {
			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Update board
						emptyBoard();
					}
				});
	
		return ad_builder.create();
	}

	//--------------------------------------------------------------
	// Get current board as a JSONArray
	//--------------------------------------------------------------
	public JSONArray getBoardAsJSON(){
		JSONArray JSONboard = new JSONArray();
		
		try{
			for(int i = 0; i < 3; i++){
				JSONArray outer_row = new JSONArray();
				for(int j = 0; j < 3; j++){
					JSONArray inner_col = new JSONArray();
					for(int k = 0; k < 3; k++){
						JSONArray inner_row = new JSONArray();
						for(int l = 0; l < 3; l++){
							inner_row.put(l, "" + board.outer_board[i][j]
										  				.getSpace(k, l));
						}
						inner_col.put(k, inner_row);
					}
					outer_row.put(j, inner_col);
				}
				JSONboard.put(i, outer_row);
			}
		} catch(Exception e){
			Log.e("GameBoardFragment", e.toString());
			Toast.makeText(getActivity(), "An error has occured...", Toast.LENGTH_SHORT).show();
		}
		
		return JSONboard;
	}

	//---------------------------------------------------------
	// Initialize board with JSONArray
	//---------------------------------------------------------
	public void initBoard(JSONArray JSON_board){
		try{
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					for(int k = 0; k < 3; k++){
						for(int l = 0; l < 3; l++){
							if(JSON_board
								.getJSONArray(i)
								.getJSONArray(j)
								.getJSONArray(k)
								.getString(l)
								.charAt(0) != Board.BLANK_TILE)
							{
								board.current_player =
								JSON_board
									.getJSONArray(i)
									.getJSONArray(j)
									.getJSONArray(k)
									.getString(l)
									.charAt(0);
								if(move(i,j,k,l)){
									// Move successful
								}
							}
						}
					}
				}
			}
			
			// Check for winner
			if(board.getWinner() != Board.BLANK_TILE){
				// Show who won and ask to tart a new game
				buildEndGameAlert(""+board.getWinner()).show();
			}
			
			// Update turn text view
			updateTurnTv();

		} catch(Exception e){
			Log.e(TAG, e.toString());
			// Let the user know something went wrong
			Toast.makeText(getActivity(), "An error occured while creating the game board...", 
					Toast.LENGTH_LONG).show();
		}
	}
	
	//--------------------------------------------------------
	// Clear everything being used to maintain the game board
	//--------------------------------------------------------
	public void emptyBoard(){
		try{
			// Reset board
			Log.i(TAG,"Resetting board");
			board = new Board();
			
			// Update game
			Log.i(TAG,"Resetting game");
			gbActivity.current_game.board = new JSONArray(DBFunct.EMPTY_JSON_BOARD);
			
			// Update db if not local game
			Log.i(TAG,"Resetting db");
			if(!gbActivity.current_game.obj_id.equals(GameBoard.LOCAL_GAME)){
				DBFunct.updateGame(gbActivity.current_game);
			}
			
			// Update UI
			Log.i(TAG,"Resetting UI");
			for(int i =0; i < 3; i++){
				for(int j =0; j < 3; j++){
					for(int k =0; k < 3; k++){
						for(int l =0; l < 3; l++){
							//if not already blank{
								spaces[i][j][k][l].setImageDrawable(getResources()
										.getDrawable(R.drawable.blank_tile));
							//}
						}
					}
				}
			}
			// Reset winner tiles
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					if(sub_winners[i][j].getVisibility() == View.VISIBLE){
						sub_winners[i][j].setVisibility(View.GONE);
						for(int k = 0; k < 3; k++){
							for(int l = 0; l < 3; l++){
								spaces[i][j][k][l].setVisibility(View.VISIBLE);
							}
						}
					}
				}
			}
			
			// Update turn text view
			updateTurnTv();
			
		} catch(Exception e){
			Log.e(TAG,e.toString());
		}
	}
	
	//----------------------------------------------------------------------
	// Make a move at the provided coordinates
	//----------------------------------------------------------------------
	protected boolean move(int i, int j, int k, int l){
		if(board.makeMove(i, j, k, l)){
			// Note: current_player is now the next player

			// Update board UI
			if(board.current_player == Board.O_TILE){
				spaces[i][j][k][l].setImageDrawable(getResources()
						.getDrawable(R.drawable.x_tile_w_bg));
			}
			if(board.current_player == Board.X_TILE){
				spaces[i][j][k][l].setImageDrawable(getResources()
						.getDrawable(R.drawable.o_tile_w_bg));
			}
			
			// Check for subgame winners
			if(board.outer_board[i][j].getWinner() == Board.X_TILE){
				for(int c1 = 0; c1 < 3; c1++){
					for(int c2 = 0; c2 < 3; c2++){
						spaces[i][j][c1][c2].setVisibility(View.GONE);
					}
				}
				sub_winners[i][j].setImageDrawable(getResources()
						.getDrawable(R.drawable.x_tile_w_bg));
				sub_winners[i][j].setVisibility(View.VISIBLE);
			}
			if(board.outer_board[i][j].getWinner() == Board.O_TILE){
				for(int c1 = 0; c1 < 3; c1++){
					for(int c2 = 0; c2 < 3; c2++){
						spaces[i][j][c1][c2].setVisibility(View.GONE);
					}
				}
				sub_winners[i][j].setImageDrawable(getResources()
						.getDrawable(R.drawable.o_tile_w_bg));
				sub_winners[i][j].setVisibility(View.VISIBLE);
			}
			
			//check for winner
			if(board.getWinner() != Board.BLANK_TILE){
				// Show who won and ask to tart a new game
				buildEndGameAlert(""+board.getWinner()).show();
			}
			return true;
		}
		return false;
	}
	
	public void makeMove(int i, int j, int k, int l){
		if(gbActivity.current_game.current_player_id 
				.equals(DBFunct.getUser().facebook_id)){
			if(move(i ,j ,k ,l)){
				// Update current game
				gbActivity.current_game.board = getBoardAsJSON();
				gbActivity.current_game.SwapPlayers();
				
				// Update database
				if(gbActivity.current_game.obj_id != GameBoard.LOCAL_GAME){
					DBFunct.updateGame(gbActivity.current_game);
					// Send push notification
					DBFunct.sendGameboardPush(gbActivity.current_game, 
					"" + i + "," + j + "," + k + "," + l);
				}
				
				// Update turn text view
				updateTurnTv();
			}
		}
	}
	
	protected void receiveMove(int i, int j, int k, int l){
		// Make move
		move(i,j,k,l);
		
		// Update current game
		gbActivity.current_game.board = getBoardAsJSON();
		gbActivity.current_game.SwapPlayers();
		
		// Update turn text view
		updateTurnTv();
	}
	
	//-----------------------------------------------------------
	// Updates text view to notify user if it is their turn
	//-----------------------------------------------------------
	protected void updateTurnTv(){
		if(gbActivity.current_game.current_player_id.equals(DBFunct.getUser().facebook_id)){
			turn_tv.setVisibility(View.VISIBLE);
		}
		else{
			turn_tv.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		Log.i(TAG,"onStart");
		
		// Get handle on activity
		gbActivity = (GameBoard) getActivity();
		
		//-------------------------------------------------------------------------
		// Get handle on image views
		//-------------------------------------------------------------------------
		
		//Board Coordinates Reference
		//        0         1         2
		//     0  1  2   0  1  2   0  1  2
		//
		//  0  00|10|20  30|40|50  60|70|80
		//     --------  --------  --------
		//0 1  01|11|21  31|41|51  61|71|81
		//     --------  --------  --------
		//  2  02|12|22  32|42|52  62|72|82
		//
		//  0  03|13|23  33|43|53  63|73|83
		//     --------  --------  --------
		//1 1  04|14|24  34|44|54  64|74|84
		//     --------  --------  --------
		//  2  05|15|25  35|45|55  65|75|85
		//
		//  0  06|16|26  36|46|56  66|76|86
		//     --------  --------  --------
		//2 1  07|17|27  37|47|57  67|77|87
		//     --------  --------  --------
		//  2  08|18|28  38|48|58  68|78|88
		
		sub_winners[0][0] = (ImageView) getActivity().findViewById(R.id.winner00);
		sub_winners[0][1] = (ImageView) getActivity().findViewById(R.id.winner01);
		sub_winners[0][2] = (ImageView) getActivity().findViewById(R.id.winner02);
		sub_winners[1][0] = (ImageView) getActivity().findViewById(R.id.winner10);
		sub_winners[1][1] = (ImageView) getActivity().findViewById(R.id.winner11);
		sub_winners[1][2] = (ImageView) getActivity().findViewById(R.id.winner12);
		sub_winners[2][0] = (ImageView) getActivity().findViewById(R.id.winner20);
		sub_winners[2][1] = (ImageView) getActivity().findViewById(R.id.winner21);
		sub_winners[2][2] = (ImageView) getActivity().findViewById(R.id.winner22);
		
		spaces[0][0][0][0] = (ImageView) getActivity().findViewById(R.id.space00);
		spaces[0][0][0][1] = (ImageView) getActivity().findViewById(R.id.space01);
		spaces[0][0][0][2] = (ImageView) getActivity().findViewById(R.id.space02);
		spaces[0][0][1][0] = (ImageView) getActivity().findViewById(R.id.space10);
		spaces[0][0][1][1] = (ImageView) getActivity().findViewById(R.id.space11);
		spaces[0][0][1][2] = (ImageView) getActivity().findViewById(R.id.space12);
		spaces[0][0][2][0] = (ImageView) getActivity().findViewById(R.id.space20);
		spaces[0][0][2][1] = (ImageView) getActivity().findViewById(R.id.space21);
		spaces[0][0][2][2] = (ImageView) getActivity().findViewById(R.id.space22);
		
		spaces[0][1][0][0] = (ImageView) getActivity().findViewById(R.id.space03);
		spaces[0][1][0][1] = (ImageView) getActivity().findViewById(R.id.space04);
		spaces[0][1][0][2] = (ImageView) getActivity().findViewById(R.id.space05);
		spaces[0][1][1][0] = (ImageView) getActivity().findViewById(R.id.space13);
		spaces[0][1][1][1] = (ImageView) getActivity().findViewById(R.id.space14);
		spaces[0][1][1][2] = (ImageView) getActivity().findViewById(R.id.space15);
		spaces[0][1][2][0] = (ImageView) getActivity().findViewById(R.id.space23);
		spaces[0][1][2][1] = (ImageView) getActivity().findViewById(R.id.space24);
		spaces[0][1][2][2] = (ImageView) getActivity().findViewById(R.id.space25);
		
		spaces[0][2][0][0] = (ImageView) getActivity().findViewById(R.id.space06);
		spaces[0][2][0][1] = (ImageView) getActivity().findViewById(R.id.space07);
		spaces[0][2][0][2] = (ImageView) getActivity().findViewById(R.id.space08);
		spaces[0][2][1][0] = (ImageView) getActivity().findViewById(R.id.space16);
		spaces[0][2][1][1] = (ImageView) getActivity().findViewById(R.id.space17);
		spaces[0][2][1][2] = (ImageView) getActivity().findViewById(R.id.space18);
		spaces[0][2][2][0] = (ImageView) getActivity().findViewById(R.id.space26);
		spaces[0][2][2][1] = (ImageView) getActivity().findViewById(R.id.space27);
		spaces[0][2][2][2] = (ImageView) getActivity().findViewById(R.id.space28);
		
		spaces[1][0][0][0] = (ImageView) getActivity().findViewById(R.id.space30);
		spaces[1][0][0][1] = (ImageView) getActivity().findViewById(R.id.space31);
		spaces[1][0][0][2] = (ImageView) getActivity().findViewById(R.id.space32);
		spaces[1][0][1][0] = (ImageView) getActivity().findViewById(R.id.space40);
		spaces[1][0][1][1] = (ImageView) getActivity().findViewById(R.id.space41);
		spaces[1][0][1][2] = (ImageView) getActivity().findViewById(R.id.space42);
		spaces[1][0][2][0] = (ImageView) getActivity().findViewById(R.id.space50);
		spaces[1][0][2][1] = (ImageView) getActivity().findViewById(R.id.space51);
		spaces[1][0][2][2] = (ImageView) getActivity().findViewById(R.id.space52);
		
		spaces[1][1][0][0] = (ImageView) getActivity().findViewById(R.id.space33);
		spaces[1][1][0][1] = (ImageView) getActivity().findViewById(R.id.space34);
		spaces[1][1][0][2] = (ImageView) getActivity().findViewById(R.id.space35);
		spaces[1][1][1][0] = (ImageView) getActivity().findViewById(R.id.space43);
		spaces[1][1][1][1] = (ImageView) getActivity().findViewById(R.id.space44);
		spaces[1][1][1][2] = (ImageView) getActivity().findViewById(R.id.space45);
		spaces[1][1][2][0] = (ImageView) getActivity().findViewById(R.id.space53);
		spaces[1][1][2][1] = (ImageView) getActivity().findViewById(R.id.space54);
		spaces[1][1][2][2] = (ImageView) getActivity().findViewById(R.id.space55);
		
		spaces[1][2][0][0] = (ImageView) getActivity().findViewById(R.id.space36);
		spaces[1][2][0][1] = (ImageView) getActivity().findViewById(R.id.space37);
		spaces[1][2][0][2] = (ImageView) getActivity().findViewById(R.id.space38);
		spaces[1][2][1][0] = (ImageView) getActivity().findViewById(R.id.space46);
		spaces[1][2][1][1] = (ImageView) getActivity().findViewById(R.id.space47);
		spaces[1][2][1][2] = (ImageView) getActivity().findViewById(R.id.space48);
		spaces[1][2][2][0] = (ImageView) getActivity().findViewById(R.id.space56);
		spaces[1][2][2][1] = (ImageView) getActivity().findViewById(R.id.space57);
		spaces[1][2][2][2] = (ImageView) getActivity().findViewById(R.id.space58);
		
		spaces[2][0][0][0] = (ImageView) getActivity().findViewById(R.id.space60);
		spaces[2][0][0][1] = (ImageView) getActivity().findViewById(R.id.space61);
		spaces[2][0][0][2] = (ImageView) getActivity().findViewById(R.id.space62);
		spaces[2][0][1][0] = (ImageView) getActivity().findViewById(R.id.space70);
		spaces[2][0][1][1] = (ImageView) getActivity().findViewById(R.id.space71);
		spaces[2][0][1][2] = (ImageView) getActivity().findViewById(R.id.space72);
		spaces[2][0][2][0] = (ImageView) getActivity().findViewById(R.id.space80);
		spaces[2][0][2][1] = (ImageView) getActivity().findViewById(R.id.space81);
		spaces[2][0][2][2] = (ImageView) getActivity().findViewById(R.id.space82);
		
		spaces[2][1][0][0] = (ImageView) getActivity().findViewById(R.id.space63);
		spaces[2][1][0][1] = (ImageView) getActivity().findViewById(R.id.space64);
		spaces[2][1][0][2] = (ImageView) getActivity().findViewById(R.id.space65);
		spaces[2][1][1][0] = (ImageView) getActivity().findViewById(R.id.space73);
		spaces[2][1][1][1] = (ImageView) getActivity().findViewById(R.id.space74);
		spaces[2][1][1][2] = (ImageView) getActivity().findViewById(R.id.space75);
		spaces[2][1][2][0] = (ImageView) getActivity().findViewById(R.id.space83);
		spaces[2][1][2][1] = (ImageView) getActivity().findViewById(R.id.space84);
		spaces[2][1][2][2] = (ImageView) getActivity().findViewById(R.id.space85);
		
		spaces[2][2][0][0] = (ImageView) getActivity().findViewById(R.id.space66);
		spaces[2][2][0][1] = (ImageView) getActivity().findViewById(R.id.space67);
		spaces[2][2][0][2] = (ImageView) getActivity().findViewById(R.id.space68);
		spaces[2][2][1][0] = (ImageView) getActivity().findViewById(R.id.space76);
		spaces[2][2][1][1] = (ImageView) getActivity().findViewById(R.id.space77);
		spaces[2][2][1][2] = (ImageView) getActivity().findViewById(R.id.space78);
		spaces[2][2][2][0] = (ImageView) getActivity().findViewById(R.id.space86);
		spaces[2][2][2][1] = (ImageView) getActivity().findViewById(R.id.space87);
		spaces[2][2][2][2] = (ImageView) getActivity().findViewById(R.id.space88);
		
		turn_tv = (TextView) getActivity().findViewById(R.id.tv_your_turn);
		turn_tv.setVisibility(View.INVISIBLE);
		
		//----------------------------------------------------------
		// Set onClick listeners
		//----------------------------------------------------------
		spaces[0][0][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,0,0);
			}
		});
		spaces[0][0][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,0,1);
			}
		});
		spaces[0][0][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,0,2);
			}
		});
		spaces[0][0][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,1,0);
			}
		});
		spaces[0][0][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,1,1);
			}
		});
		spaces[0][0][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,1,2);
			}
		});
		spaces[0][0][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,2,0);
			}
		});
		spaces[0][0][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,2,1);
			}
		});
		spaces[0][0][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,0,2,2);
			}
		});
		spaces[0][1][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,0,0);
			}
		});
		spaces[0][1][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,0,1);
			}
		});
		spaces[0][1][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,0,2);
			}
		});
		spaces[0][1][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,1,0);
			}
		});
		spaces[0][1][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,1,1);
			}
		});
		spaces[0][1][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,1,2);
			}
		});
		spaces[0][1][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,2,0);
			}
		});
		spaces[0][1][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,2,1);
			}
		});
		spaces[0][1][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,1,2,2);
			}
		});
		spaces[0][2][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,0,0);
			}
		});
		spaces[0][2][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,0,1);
			}
		});
		spaces[0][2][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,0,2);
			}
		});
		spaces[0][2][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,1,0);
			}
		});
		spaces[0][2][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,1,1);
			}
		});
		spaces[0][2][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,1,2);
			}
		});
		spaces[0][2][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,2,0);
			}
		});
		spaces[0][2][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,2,1);
			}
		});
		spaces[0][2][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(0,2,2,2);
			}
		});
		spaces[1][0][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,0,0);
			}
		});
		spaces[1][0][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,0,1);
			}
		});
		spaces[1][0][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,0,2);
			}
		});
		spaces[1][0][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,1,0);
			}
		});
		spaces[1][0][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,1,1);
			}
		});
		spaces[1][0][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,1,2);
			}
		});
		spaces[1][0][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,2,0);
			}
		});
		spaces[1][0][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,2,1);
			}
		});
		spaces[1][0][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,0,2,2);
			}
		});
		spaces[1][1][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,0,0);
			}
		});
		spaces[1][1][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,0,1);
			}
		});
		spaces[1][1][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,0,2);
			}
		});
		spaces[1][1][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,1,0);
			}
		});
		spaces[1][1][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,1,1);
			}
		});
		spaces[1][1][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,1,2);
			}
		});
		spaces[1][1][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,2,0);
			}
		});
		spaces[1][1][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,2,1);
			}
		});
		spaces[1][1][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,1,2,2);
			}
		});
		spaces[1][2][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,0,0);
			}
		});
		spaces[1][2][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,0,1);
			}
		});
		spaces[1][2][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,0,2);
			}
		});
		spaces[1][2][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,1,0);
			}
		});
		spaces[1][2][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,1,1);
			}
		});
		spaces[1][2][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,1,2);
			}
		});
		spaces[1][2][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,2,0);
			}
		});
		spaces[1][2][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,2,1);
			}
		});
		spaces[1][2][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(1,2,2,2);
			}
		});
		spaces[2][0][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,0,0);
			}
		});
		spaces[2][0][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,0,1);
			}
		});
		spaces[2][0][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,0,2);
			}
		});
		spaces[2][0][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,1,0);
			}
		});
		spaces[2][0][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,1,1);
			}
		});
		spaces[2][0][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,1,2);
			}
		});
		spaces[2][0][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,2,0);
			}
		});
		spaces[2][0][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,2,1);
			}
		});
		spaces[2][0][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,0,2,2);
			}
		});
		spaces[2][1][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,0,0);
			}
		});
		spaces[2][1][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,0,1);
			}
		});
		spaces[2][1][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,0,2);
			}
		});
		spaces[2][1][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,1,0);
			}
		});
		spaces[2][1][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,1,1);
			}
		});
		spaces[2][1][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,1,2);
			}
		});
		spaces[2][1][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,2,0);
			}
		});
		spaces[2][1][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,2,1);
			}
		});
		spaces[2][1][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,1,2,2);
			}
		});
		spaces[2][2][0][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,0,0);
			}
		});
		spaces[2][2][0][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,0,1);
			}
		});
		spaces[2][2][0][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,0,2);
			}
		});
		spaces[2][2][1][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,1,0);
			}
		});
		spaces[2][2][1][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,1,1);
			}
		});
		spaces[2][2][1][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,1,2);
			}
		});
		spaces[2][2][2][0].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,2,0);
			}
		});
		spaces[2][2][2][1].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,2,1);
			}
		});
		spaces[2][2][2][2].setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				makeMove(2,2,2,2);
			}
		});
		if(gbActivity.current_game != null){
			initBoard(gbActivity.current_game.board);
		}
		else{
			// Let the user know something went wrong
			Toast.makeText(getActivity(), "An error has occured while creating the game...", 
					Toast.LENGTH_SHORT).show();
		}
	}
}
