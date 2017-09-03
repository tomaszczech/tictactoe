package com.czecht.tictactoe.domain.game;

import com.czecht.tictactoe.ddd.model.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

// should be VO
@Getter
@AllArgsConstructor
public class GameFinished implements Event {

	private String gameId;

}
