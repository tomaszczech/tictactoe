package com.czecht.tictactoe.domain.game;

import java.util.concurrent.ThreadLocalRandom;

import org.joda.time.DateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Game {

	@Setter
	private long id;

	private String player0;

	private String player1;

	private String currentPlayer;

	private DateTime startDate;

	private Movement prevMovement;

	@Getter(AccessLevel.NONE)
	private Board board;

	public Game(String player0, String player1, String startPlayer) {
		this.player0 = player0;
		this.player1 = player1;
		this.currentPlayer = player0.equals(startPlayer) ? player1 : player0;
		this.board = new Board();
		this.startDate = new DateTime();
	}

	public void makeMove(Movement movement) {
		board.markPosition(movement.getX(), movement.getY(), convertPlayer(movement.getPlayer()));
		this.prevMovement = movement;
		this.currentPlayer = movement.getPlayer();
	}

	public GameStatus checkGameStatus() {
		return board.checkBoardState(this.prevMovement.getX(),
				this.prevMovement.getY(),
				convertPlayer(this.prevMovement.getPlayer()));
	}

	private int convertPlayer(String player) {
		return player.equals(player0) ? 0 : 1;
	}

	public Integer[][] getBoard() {
		return board.getBoard();
	}

	public String getCurrentPlayer() {
		return player0.equals(currentPlayer) ? player0 : player1;
	}

	public String getNextPlayer() {
		return player0.equals(currentPlayer) ? player1 : player0;
	}

	public String getPlayerX() {
		return player1;
	}

	public String getPlayerO() {
		return player0;
	}

	public static Game getInstance(String first, String second) {
		int randomNum = ThreadLocalRandom.current().nextInt(0, 1);
		return randomNum == 0 ? new Game(first, second, first) : new Game(first, second, second);
	}
}
