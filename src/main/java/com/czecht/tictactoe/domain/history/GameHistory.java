package com.czecht.tictactoe.domain.history;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameHistory {

	public enum GameResult {
		WIN, DRAW
	}

	@Id
	@GeneratedValue
	private long id;

	private String playerCircle;

	private String playerCross;

	private Date endGame;

	@Enumerated
	private GameResult result;

	private String winner;

}
