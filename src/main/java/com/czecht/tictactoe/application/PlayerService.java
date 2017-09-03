package com.czecht.tictactoe.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.czecht.tictactoe.ddd.annotations.application.ApplicationService;
import com.czecht.tictactoe.infrastructure.storage.GameStorage;
import com.czecht.tictactoe.infrastructure.storage.PlayerStorage;
import com.czecht.tictactoe.shared.DomainOperationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationService
public class PlayerService {

	private final PlayerStorage playerStorage;

	private final GameStorage gameStorage;

	@Autowired
	public PlayerService(PlayerStorage playerStorage, GameStorage gameStorage) {
		this.playerStorage = playerStorage;
		this.gameStorage = gameStorage;
	}

	public String loginPlayer(String player) {
		if(StringUtils.isEmpty(player)) {
			throw new DomainOperationException("Player login can not be empty.");
		}

		if(playerStorage.isPlayerExists(player)) {
			return null;
		}

		return playerStorage.addPlayer(player);
	}

	public void logoutPlayer(String player) {
		playerStorage.removePlayer(player);
	}

	//todo: consider immutable collection
	public Set<String> findAvailablePlayersForGame() {
		Set<String> allPlayers = new HashSet<>(playerStorage.findAll());
		Set<String> playingPlayers = gameStorage.findAllPlayingPlayers();
		allPlayers.removeAll(playingPlayers);
		allPlayers.remove(getCurrentUser());
		return allPlayers;
	}

	public String getCurrentUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (String)context.getExternalContext().getSessionMap().get("user");
	}

	public boolean isPlayerLogged(String player) {
		return playerStorage.isPlayerExists(player);
	}

	public String getRandomPlayerForGame() {
		Set<String> players = new HashSet<>(findAvailablePlayersForGame());
		if(players.isEmpty()) {
			return null;
		}

		List<String> listToRandomPlayer = new ArrayList<>();
		listToRandomPlayer.addAll(players);

		int randomIndex=0;
		if(players.size()>1) {
			randomIndex = ThreadLocalRandom.current().nextInt(0, players.size() - 1);
		}

		return listToRandomPlayer.get(randomIndex);
	}
}
