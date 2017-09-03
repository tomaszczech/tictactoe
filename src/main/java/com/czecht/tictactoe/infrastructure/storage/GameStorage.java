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

	private String GAME_ID = "GAME_%s";

	private Map<String, Game> games = new HashMap<>();

	public Game addGame(Game game) {
		game.setId(generateId());
		games.put(game.getId(), game);
		return game;
	}

	public void delete(Game game) {
		game.deactivate();
		games.put(game.getId(), game);
	}

	public Set<String> findAllPlayingPlayers() {
		Set<String> players = new HashSet<>();

		for(Game game : games.values()) {
			if(game.isActiveGame()) {
				players.add(game.getPlayerCircle());
				players.add(game.getPlayerCross());
			}
		}

		return players;
	}

	public Game findGameById(String gameId) {
		return games.get(gameId);
	}

	public boolean isPlayerInGame(String player) {
		return games.values()
				.stream()
				.anyMatch(game -> game.isActiveGame() && (game.getPlayerCircle().equals(player) || game.getPlayerCross()
						.equals(player)));

	}

	private String generateId() {
		idSequence++;
		return String.format(GAME_ID, idSequence);
	}
}
