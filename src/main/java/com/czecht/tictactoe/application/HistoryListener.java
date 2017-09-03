package com.czecht.tictactoe.application;

import org.springframework.stereotype.Component;

import com.czecht.tictactoe.domain.game.GameFinished;
import com.google.common.eventbus.Subscribe;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HistoryListener {

	private final HistoryService historyService;

	public HistoryListener(HistoryService historyService) {
		this.historyService = historyService;
	}

	@Subscribe
	public void on(GameFinished event) {
		historyService.save(event.getGameId());
	}
}
