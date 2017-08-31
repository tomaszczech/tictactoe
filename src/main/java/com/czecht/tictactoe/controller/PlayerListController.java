package com.czecht.tictactoe.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.czecht.tictactoe.application.GameService;
import com.czecht.tictactoe.application.PlayerService;
import com.czecht.tictactoe.domain.game.Game;

import lombok.Getter;

@Scope(value = "session")
@Component(value = "playerListController")
@ELBeanName(value = "playerListController")
@Join(path = "/game/players", to = "/game/player-list.jsf")
public class PlayerListController {

	private static final String NEW_GAME_URL = "/game/board.jsf?faces-redirect=true&gameId=%s";

	private final PlayerService playerService;

	private final GameService gameService;

	@Getter
	private Set<String> players;

	@Autowired
	public PlayerListController(PlayerService playerService, GameService gameService) {
		this.playerService = playerService;
		this.gameService = gameService;
	}

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		players = new HashSet<>();
		players = playerService.findAvailablePlayersForGame();
	}

	public String playPlayerGame(String player) {
		Game game = gameService.createGame(player);

		return String.format(NEW_GAME_URL, game.getId());
	}

	public String playRandomGame() {
		Game game = gameService.createRandomGame();
		return String.format(NEW_GAME_URL, game.getId());
	}

	public void handleNewGameAction() throws IOException {
		String gameId = FacesContext.getCurrentInstance()
				.getExternalContext()
				.getRequestParameterMap()
				.get("newGameId");
		FacesContext.getCurrentInstance().getExternalContext().redirect(String.format(NEW_GAME_URL, gameId));
	}
}
