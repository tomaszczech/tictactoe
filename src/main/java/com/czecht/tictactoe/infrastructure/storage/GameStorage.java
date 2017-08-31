package com.czecht.tictactoe.infrastructure.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import com.czecht.tictactoe.domain.game.Game;

@ApplicationScope
@Component
public class GameStorage {

	private Long idSequence = 0L;

	private Map<Long, Game> games = new HashMap<>();

	public void addGame(Game game) {
		Long gameId = idSequence++;

		game.setId(gameId);
		games.put(gameId, game);
	}

	public void delete(Game game) {
		games.remove(game.getId());
	}

	public Set<String> findAllPlayingPlayers() {
		Set<String> players = new HashSet<>();

		for(Game game : games.values()) {
			players.add(game.getPlayer0());
			players.add(game.getPlayer1());
		}

		return players;
	}

	public Game findGameById(Long gameId) {
		return games.get(gameId);
	}

}
