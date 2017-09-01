package com.czecht.tictactoe.domain.game;

import com.czecht.tictactoe.shared.DomainOperationException;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Board {

	private int boardSize = 3;

	private int moveCount = 0;

	@Getter
	private Integer[][] board = new Integer[boardSize][boardSize];

	public Board(Integer[][] board) {
		this.board = board;
	}

	public void markPosition(int x, int y, int state) {
		if(x < 0 || x > boardSize || y < 0 || y > boardSize) {
			throw new DomainOperationException(String.format("Illegal position [%s][%s]", x, y));
		}
		if(board[x][y] != null) {
			throw new DomainOperationException(String.format("The position [%s][%s] is already marked.", x, y));
		}

		board[x][y] = state;
		moveCount++;
	}

	public GameStatus checkBoardState(int x, int y, int state) {

		// kolumny
		for(int i = 0; i < boardSize; i++) {
			if(board[x][i] == null || (board[x][i] != state)) {
				break;
			}
			if(i == boardSize - 1) {
				return GameStatus.WIN;
			}
		}

		// wiersze
		for(int i = 0; i < boardSize; i++) {
			if(board[i][y] == null || (board[i][y] != state)) {
				break;
			}
			if(i == boardSize - 1) {
				return GameStatus.WIN;
			}
		}

		// srodek
		if(x == y) {
			for(int i = 0; i < boardSize; i++) {
				if(board[i][i] == null || (board[i][i] != state)) {
					break;
				}
				if(i == boardSize - 1) {
					return GameStatus.WIN;
				}
			}
		}

		//check anti diag
		if(x + y == boardSize - 1) {
			for(int i = 0; i < boardSize; i++) {
				if(board[i][(boardSize - 1) - i] == null || (board[i][(boardSize - 1) - i] != state)) {
					break;
				}
				if(i == boardSize - 1) {
					return GameStatus.WIN;
				}
			}
		}

		// remis
		if(moveCount == (boardSize * boardSize)) {
			return GameStatus.DRAW;
		}

		return GameStatus.CONTINOUE;
	}
}
