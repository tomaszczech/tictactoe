package com.czecht.tictactoe.controller;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.czecht.tictactoe.application.GameService;
import com.czecht.tictactoe.domain.game.Game;
import com.czecht.tictactoe.domain.game.GameStatus;
import com.czecht.tictactoe.domain.game.Movement;

import lombok.Getter;
import lombok.Setter;

@Scope(value = "session")
@Component(value = "boardController")
@ELBeanName(value = "boardController")
@Join(path = "/game/board/{gameId}", to = "/game/board.jsf?gameId={gameId}")
public class BoardController {

	@Getter
	@Setter
	private String gameId;

	@Getter
	private Game game;

	@Autowired
	private GameService gameService;

	@Autowired
	private LoginController loginController;

	public BoardController() {

	}

	public boolean isMoveDisabled() {
		String currentPlayer = loginController.getUsername();
		String prevPlayer = game.getCurrentPlayer();

		return !currentPlayer.equals(prevPlayer);
	}

	public void init() {
		game = gameService.findGameById(Long.valueOf(gameId));
	}

	public void makeMove() throws IOException {
		String x = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("x");
		String y = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("y");
		gameService.makeMove(game, new Movement(Integer.valueOf(x), Integer.valueOf(y), loginController.getUsername()));
		game = gameService.findGameById(Long.valueOf(gameId));
	}

	public String convertSign(Integer sign) {
		if(sign == null) {
			return "";
		}
		return sign == 0 ? "O" : "X";
	}
}
