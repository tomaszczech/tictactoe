package com.czecht.tictactoe.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.czecht.tictactoe.application.GameService;
import com.czecht.tictactoe.domain.history.GameHistory;

import lombok.Getter;
import lombok.Setter;

@Scope(value = "session")
@Component(value = "historyController")
@ELBeanName(value = "historyController")
@Join(path = "/game/history/{player}", to = "/game/history.jsf?player={player}")
public class HistoryController {

	@Getter
	@Setter
	private String player;

	@Getter
	private List<GameHistory> history;

	private final GameService gameService;

	@Autowired
	public HistoryController(GameService gameService) {
		this.gameService = gameService;
	}

	public void init() {
		if(StringUtils.isEmpty(player)) {
			loadAllHistory();
		} else {
			loadHistoryForPlayer(player);
		}
	}

	public void loadAllHistory() {
		history = new ArrayList<>(gameService.findAllHistory());
	}

	public void loadHistoryForPlayer(String player) {
		history = new ArrayList<>(gameService.findHistoryForPlayer(player));
	}

}
