package com.czecht.tictactoe.domain.history;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HISTORY")
public class GameHistory {

	public enum GameResult {
		WIN, DRAW
	}

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String playerCircle;

	@Column
	private String playerCross;

	@Column
	private long gameTime;

	@Enumerated(EnumType.STRING)
	private GameResult result;

	@Column
	private String winner;

}
