package com.czecht.tictactoe.domain.game;

import org.springframework.stereotype.Component;

@Component
public class GameFactory {

	public Game createGame(String first, String second) {
		return new Game(first, second, first);
	}

}
