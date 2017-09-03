package com.czecht.tictactoe.domain.game;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

//aggregate root
@Getter
public class Game {

	enum Status {ACTIVE, INACTIVE}

	@Setter
	private String id;

	private String playerCircle;

	private String playerCross;

	private String currentPlayer;

	@Getter(AccessLevel.NONE)
	private DateTime startDate;

	private Movement prevMovement;

	@Getter(AccessLevel.NONE)
	private Board board;

	private Status status = Status.ACTIVE;

	public Game(String playerCircle, String playerCross, String startPlayer) {
		this.playerCircle = playerCircle;
		this.playerCross = playerCross;
		this.currentPlayer = playerCircle.equals(startPlayer) ? playerCross : playerCircle;
		this.board = new Board();
		this.startDate = new DateTime();
	}

	public void makeMove(Movement movement) {
		board.markPosition(movement.getX(), movement.getY(), convertPlayer(movement.getPlayer()));
		this.prevMovement = movement;
		this.currentPlayer = movement.getPlayer();
	}

	public GameStatus checkGameStatus() {
		if(prevMovement == null) {
			return GameStatus.CONTINOUE;
		}

		return board.checkBoardState(this.prevMovement.getX(),
				this.prevMovement.getY(),
				convertPlayer(this.prevMovement.getPlayer()));
	}

	private int convertPlayer(String player) {
		return player.equals(playerCircle) ? 0 : 1;
	}

	public Integer[][] getBoard() {
		return board.getBoard();
	}

	public String getCurrentPlayer() {
		return playerCircle.equals(currentPlayer) ? playerCircle : playerCross;
	}

	public String getNextPlayer() {
		return playerCircle.equals(currentPlayer) ? playerCross : playerCircle;
	}

	public boolean isContinoueGame() {
		return GameStatus.CONTINOUE.equals(checkGameStatus());
	}

	public String getWinner() {
		if(GameStatus.WIN.equals(checkGameStatus())) {
			return prevMovement.getPlayer();
		}
		return StringUtils.EMPTY;
	}

	public long getGameTime() {
		return new DateTime().minus(startDate.getMillis()).getMillis();
	}

	public static Game getInstance(String first, String second) {
		int randomNum = ThreadLocalRandom.current().nextInt(0, 1);
		return randomNum == 0 ? new Game(first, second, first) : new Game(first, second, second);
	}

	public void deactivate() {
		this.status = Status.INACTIVE;
	}

	public boolean isActiveGame() {
		return Status.ACTIVE.equals(status);
	}
}
