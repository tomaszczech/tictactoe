package com.czecht.tictactoe.application;

import javax.faces.application.FacesMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.czecht.tictactoe.ddd.annotations.application.ApplicationService;
import com.czecht.tictactoe.domain.game.Game;
import com.czecht.tictactoe.domain.game.GameFinished;
import com.czecht.tictactoe.domain.game.GameStatus;
import com.czecht.tictactoe.domain.game.Movement;
import com.czecht.tictactoe.infrastructure.push.PushChannel;
import com.czecht.tictactoe.infrastructure.storage.GameStorage;
import com.google.common.eventbus.EventBus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationService
public class GameService extends AbstractService {

	private final GameStorage gameStorage;

	private final PlayerService playerService;

	private final EventBus eventBus;

	@Autowired
	public GameService(GameStorage gameStorage, PlayerService playerService, EventBus eventBus) {
		this.gameStorage = gameStorage;
		this.playerService = playerService;
		this.eventBus = eventBus;
	}

	/*
		consider void method and push message about game creation.
		Other handlers may handle that message (for statistics, player level etc)
	 */
	public Game createGame(String player) {

		if(canPlayerTakeGame(player)) {
			return null;
		}

		Game game = Game.getInstance(player, playerService.getCurrentUser());
		game = gameStorage.addGame(game);
		pushMessage(player, game.getId());

		log.info(String.format("New game created, game_id: %s, players [%s, %s].",
				game.getId(),
				player,
				playerService.getCurrentUser()));
		return game;
	}

	public Game createRandomGame() {
		String randomPlayer = playerService.getRandomPlayerForGame();

		if(StringUtils.isEmpty(randomPlayer)) {
			log.info("No players available.");
			return null;
		}

		Game game = Game.getInstance(randomPlayer, playerService.getCurrentUser());
		gameStorage.addGame(game);

		pushMessage(randomPlayer, game.getId());
		log.info(String.format("New random game created, game_id: %s, players [%s, %s].",
				game.getId(),
				randomPlayer,
				playerService.getCurrentUser()));

		return game;
	}

	public Game findGameById(String gameId) {
		return gameStorage.findGameById(gameId);
	}

	// push message for
	public void makeMove(Game game, Movement movement) {

		Game currentGame = gameStorage.findGameById(game.getId());
		if(!currentGame.isContinoueGame()) {
			String gameResult = currentGame.checkGameStatus().toString();
			String winner = currentGame.getWinner();
			String messageDetails = String.format("%s %s", gameResult, winner);
			FacesMessage message = new FacesMessage("Game end.", messageDetails);
			pushMessage(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayerCircle()), message);
			pushMessage(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayerCross()), message);
			return;
		}

		currentGame.makeMove(movement);
		GameStatus gameStatus = currentGame.checkGameStatus();

		if(GameStatus.CONTINOUE.equals(gameStatus)) {
			pushMessage(String.format(PushChannel.BOARD_CHANNEL, currentGame.getPlayerCross()), game.getId());
			pushMessage(String.format(PushChannel.BOARD_CHANNEL, currentGame.getPlayerCircle()), game.getId());
		} else {

			String gameResult = currentGame.checkGameStatus().toString();
			String winner = currentGame.getWinner();
			String messageDetails = String.format("%s %s", gameResult, winner);
			FacesMessage message = new FacesMessage("Game end.", messageDetails);
			pushMessage(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayerCircle()), message);
			pushMessage(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayerCross()), message);
			gameStorage.delete(currentGame);

			//handle history save
			eventBus.post(new GameFinished(currentGame.getId()));
		}
	}

	private boolean canPlayerTakeGame(String player) {
		boolean playerLogged = playerService.isPlayerLogged(player);
		boolean playerInGame = gameStorage.isPlayerInGame(player);
		return !playerLogged || playerInGame;
	}
}
