package com.czecht.tictactoe.infrastructure.storage;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class PlayerStorage {

	private Set<String> players = new HashSet<>();

	public PlayerStorage() {
		System.out.println("create PlayerStorage");
	}

	public Set<String> findAll() {
		return players;
	}

	public String addPlayer(String player) {
		players.add(player);
		return player;
	}

	public void removePlayer(String player) {
		players.remove(player);
	}

	public boolean isPlayerExists(String login) {
		Optional<String> playerOptional = players.stream().filter(player -> player.equals(login)).findFirst();

		return playerOptional.isPresent();
	}

}
