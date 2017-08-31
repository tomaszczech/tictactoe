package com.czecht.tictactoe.domain.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Movement {

	private int x;

	private int y;

	private String player;

}
