package com.czecht.tictactoe.web.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.czecht.tictactoe.readmodel.HistoryDto;
import com.czecht.tictactoe.readmodel.HistoryFinder;

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
	private List<HistoryDto> history;

	private final HistoryFinder historyFinder;

	@Autowired
	public HistoryController(HistoryFinder historyFinder) {
		this.historyFinder = historyFinder;
	}

	public void init() {
		if(StringUtils.isEmpty(player)) {
			loadAllHistory();
		} else {
			loadHistoryForPlayer(player);
		}
	}

	public void loadAllHistory() {
		history = historyFinder.findAll();
	}

	public void loadHistoryForPlayer(String player) {
		history = historyFinder.findByPlayer(player);
	}

}
