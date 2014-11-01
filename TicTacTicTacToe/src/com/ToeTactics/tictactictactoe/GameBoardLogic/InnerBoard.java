package com.ToeTactics.tictactictactoe.GameBoardLogic;

public class InnerBoard{
	private char[][] inner_board = new char[3][3];
	private char winner = ' ';

	///////////////////////////////////
	//constructor
	//////////////////////////////////
	public InnerBoard(){
		//initialize each space
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				inner_board[i][j] = ' ';
			}
		}
	}


	///////////////////////////////////////////////
	//returns true if space is not taken
	//////////////////////////////////////////////
	public boolean setSpace(int x, int y, char p){
		if(inner_board[x][y] == ' '){
			inner_board[x][y] = p;
			if(checkForWinner('X')){
				winner = 'X';
			}
			else if(checkForWinner('O')){
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
	//returns true if no ' ' found
	//false otherwise
	//////////////////////////////////////
	public boolean checkIfBoardFull(){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(inner_board[i][j] == ' ')
					return false;
			}
		}
		
		return true;
	}
	
	//////////////////////////////////
	//returns value of space
	//'X' if player X
	//'O' if player O
	//' ' if empty
	/////////////////////////////////
	public char getSpace(int x, int y){
		return inner_board[x][y];
	}
	
	/////////////////////////////////
	//returns winner
	//'X' if player X
	//'Y' if player Y
	//' ' if neither
	//'T' is tie
	////////////////////////////////
	public char getWinner(){
		return winner;
	}
}
