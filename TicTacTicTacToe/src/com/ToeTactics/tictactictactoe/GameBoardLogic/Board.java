package com.ToeTactics.tictactictactoe.GameBoardLogic;

public class Board{
	public InnerBoard[][] outer_board = new InnerBoard[3][3];
	public char current_player = 'X';
	private char winner = ' ';

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
			if(checkWinner('X')){
				winner = 'X';
			}
			else if (checkWinner('O')){
				winner = 'O';
			}
			else if(checkIfBoardFull()){
				winner = 'T';
			}
			else{
				winner = ' ';
			}

			return true;
		}
		
		return false;
	}

	///////////////////////////////////////////////////////////////
	// switches players
	///////////////////////////////////////////////////////////////
	public void switchPlayers(){
		if(current_player == 'X')
			current_player = 'O';
		else
			current_player = 'X';
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
				if(outer_board[i][j].getWinner() == ' ')
					return false;
			}
		}
		
		return true;
	}
	
	///////////////////////////////////////////////////
	// returns winner
	// 'X' if player 'X'
	// 'O' if player 'O'
	// ' ' if no winner
	// 'T' if tie
	//////////////////////////////////////////////////
	public char getWinner(){
		return winner;
	}
}
