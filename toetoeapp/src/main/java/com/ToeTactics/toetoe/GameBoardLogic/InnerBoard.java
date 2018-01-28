package com.ToeTactics.toetoe.GameBoardLogic;

public class InnerBoard{
	private char[][] inner_board = new char[3][3];
	private char winner = Board.BLANK_TILE;

	///////////////////////////////////
	//constructor
	//////////////////////////////////
	public InnerBoard(){
		//initialize each space
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				inner_board[i][j] = Board.BLANK_TILE;
			}
		}
	}


	///////////////////////////////////////////////
	//returns true if space is not taken
	//////////////////////////////////////////////
	public boolean setSpace(int x, int y, char p){
		if(inner_board[x][y] == Board.BLANK_TILE){
			inner_board[x][y] = p;
			if(checkForWinner(Board.X_TILE)){
				winner = Board.X_TILE;
			}
			else if(checkForWinner(Board.O_TILE)){
				winner = Board.O_TILE;
			}
			else if(checkIfBoardFull()){
				winner = Board.TIE_TILE;
			}
			else{
				winner = Board.BLANK_TILE;
			}
			return true;
		}
		
		return false;
	}
	
	////////////////////////////////////////
	//returns whether provided player wins
	///////////////////////////////////////
	private boolean checkForWinner(char p){
		//vertical
		if((inner_board[0][0] == p) && (inner_board[0][1] == p) && (inner_board[0][2] == p)){
			return true;
		}
		if((inner_board[1][0] == p) && (inner_board[1][1] == p) && (inner_board[1][2] == p)){
			return true;
		}
		if((inner_board[2][0] == p) && (inner_board[2][1] == p) && (inner_board[2][2] == p)){
			return true;
		}
		
		//horizontal
		if((inner_board[0][0] == p) && (inner_board[1][0] == p) && (inner_board[2][0] == p)){
			return true;
		}
		if((inner_board[0][1] == p) && (inner_board[1][1] == p) && (inner_board[2][1] == p)){
			return true;
		}
		if((inner_board[0][2] == p) && (inner_board[1][2] == p) && (inner_board[2][2] == p)){
			return true;
		}
		
		//diaginal
		if((inner_board[0][0] == p) && (inner_board[1][1] == p) && (inner_board[2][2] == p)){
			return true;
		}
		if((inner_board[0][2] == p) && (inner_board[1][1] == p) && (inner_board[2][0] == p)){
			return true;
		}
		
		return false;
	}
	
	//////////////////////////////////////
	//returns true if no Board.BLANK_TILE found
	//false otherwise
	//////////////////////////////////////
	public boolean checkIfBoardFull(){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(inner_board[i][j] == Board.BLANK_TILE)
					return false;
			}
		}
		
		return true;
	}
	
	//////////////////////////////////
	//returns value of space
	//Board.X_TILE if player X
	//Board.O_TILE if player O
	//Board.BLANK_TILE if empty
	/////////////////////////////////
	public char getSpace(int x, int y){
		return inner_board[x][y];
	}
	
	/////////////////////////////////
	//returns winner
	//Board.X_TILE if player X
	//Board.O_TILE if player O
	//Board.BLANK_TILE if neither
	//Board.TIE_TILE is tie
	////////////////////////////////
	public char getWinner(){
		return winner;
	}
}
