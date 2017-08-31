package com.czecht.tictactoe.readmodel;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HistoryDto implements Serializable {

	private String playerCircle;

	private String playerCross;

	private long gameTime;

	private String result;

	private String winner;

	public String getGameTime() {
		return String.format("%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(gameTime),
				TimeUnit.MILLISECONDS.toSeconds(gameTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(
						gameTime)));
	}

}
