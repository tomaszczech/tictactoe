package com.czecht.tictactoe.application;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.czecht.tictactoe.ddd.annotations.application.ApplicationService;
import com.czecht.tictactoe.domain.game.Game;
import com.czecht.tictactoe.domain.game.GameStatus;
import com.czecht.tictactoe.domain.history.GameHistory;
import com.czecht.tictactoe.domain.history.GameHistoryRepository;
import com.google.common.eventbus.EventBus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationService
public class HistoryService {

	private final GameHistoryRepository gameHistoryRepository;

	private final GameService gameService;

	private final EventBus eventBus;

	@Autowired
	public HistoryService(GameHistoryRepository gameHistoryRepository, GameService gameService, EventBus eventBus) {
		this.gameHistoryRepository = gameHistoryRepository;
		this.gameService = gameService;
		this.eventBus = eventBus;
	}

	@PostConstruct
	public void init() {
		eventBus.register(new HistoryListener(this));
	}

	public void save(String gameId) {
		Game game = gameService.findGameById(gameId);

		GameHistory.GameHistoryBuilder historyBuilder = GameHistory.builder()
				.gameTime(game.getGameTime())
				.playerCircle(game.getPlayerCircle())
				.playerCross(game.getPlayerCross())
				.result(convertResult(game.checkGameStatus()))
				.winner(game.getWinner());

		log.info("Save history for game: ", game.getId());
		gameHistoryRepository.save(historyBuilder.build());
	}

	private GameHistory.GameResult convertResult(GameStatus gameStatus) {
		if(GameStatus.WIN.equals(gameStatus)) {
			return GameHistory.GameResult.WIN;
		} else {
			return GameHistory.GameResult.DRAW;
		}
	}
}
