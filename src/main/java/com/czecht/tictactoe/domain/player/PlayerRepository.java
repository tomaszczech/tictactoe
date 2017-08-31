package com.czecht.tictactoe.domain.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.czecht.tictactoe.infrastructure.storage.PlayerStorage;

@Component
public class PlayerRepository {

	private final PlayerStorage playerStorage;

	@Autowired
	public PlayerRepository(PlayerStorage playerStorage) {
		this.playerStorage = playerStorage;
	}



}
