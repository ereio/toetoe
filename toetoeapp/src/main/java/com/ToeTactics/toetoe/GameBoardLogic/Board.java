package com.ToeTactics.toetoe.GameBoardLogic;

public class Board{
	public static final char X_TILE = 'X';
	public static final char O_TILE = 'O';
	public static final char BLANK_TILE = '_';
	public static final char TIE_TILE = 'T';
	
	public InnerBoard[][] outer_board = new InnerBoard[3][3];
	public char current_player = X_TILE;
	private char winner = BLANK_TILE;

	////////////////////////////////////////////////
	// constructor
	///////////////////////////////////////////////
	public Board(){
		//initialize each board
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				outer_board[i][j] = new InnerBoard();
			}
		}
	}

	//////////////////////////////////////////////////////
	// set space at x,y in board bx,by
	// returns false if space already taken
	//////////////////////////////////////////////////////
	public boolean makeMove(int bx, int by, int x, int y){
		if(outer_board[bx][by].setSpace(x,y,current_player)){
			switchPlayers();
			if(checkWinner(X_TILE)){
				winner = X_TILE;
			}
			else if (checkWinner(O_TILE)){
				winner = O_TILE;
			}
			else if(checkIfBoardFull()){
				winner = TIE_TILE;
			}
			else{
				winner = BLANK_TILE;
			}

			return true;
		}
		
		return false;
	}

	///////////////////////////////////////////////////////////////
	// switches players
	///////////////////////////////////////////////////////////////
	public void switchPlayers(){
		if(current_player == X_TILE)
			current_player = O_TILE;
		else
			current_player = X_TILE;
	}

	////////////////////////////////////////////////////////////////
	// returns true if provided player is winner
	// false otherwise
	///////////////////////////////////////////////////////////////
	public boolean checkWinner(char p){
		//vertical
		if((outer_board[0][0].getWinner() == p) && (outer_board[0][1].getWinner() == p) && (outer_board[0][2].getWinner() == p)){
			return true;
		}
		if((outer_board[1][0].getWinner() == p) && (outer_board[1][1].getWinner() == p) && (outer_board[1][2].getWinner() == p)){
			return true;
		}
		if((outer_board[2][0].getWinner() == p) && (outer_board[2][1].getWinner() == p) && (outer_board[2][2].getWinner() == p)){
			return true;
		}
		
		//horizontal
		if((outer_board[0][0].getWinner() == p) && (outer_board[1][0].getWinner() == p) && (outer_board[2][0].getWinner() == p)){
			return true;
		}
		if((outer_board[0][1].getWinner() == p) && (outer_board[1][1].getWinner() == p) && (outer_board[2][1].getWinner() == p)){
			return true;
		}
		if((outer_board[0][2].getWinner() == p) && (outer_board[1][2].getWinner() == p) && (outer_board[2][2].getWinner() == p)){
			return true;
		}
		
		//diaginal
		if((outer_board[0][0].getWinner() == p) && (outer_board[1][1].getWinner() == p) && (outer_board[2][2].getWinner() == p)){
			return true;
		}
		if((outer_board[0][2].getWinner() == p) && (outer_board[1][1].getWinner() == p) && (outer_board[2][0].getWinner() == p)){
			return true;
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////
	// returns true if each subgame has a winner or a tie
	// false otherwise
	///////////////////////////////////////////////////////
	public boolean checkIfBoardFull(){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(outer_board[i][j].getWinner() == BLANK_TILE)
					return false;
			}
		}
		
		return true;
	}
	
	///////////////////////////////////////////////////
	// returns winner
	// X_TILE if player X
	// O_TILE if player O
	// BLANK_TILE if no winner
	// 'T' if tie
	//////////////////////////////////////////////////
	public char getWinner(){
		return winner;
	}
}
