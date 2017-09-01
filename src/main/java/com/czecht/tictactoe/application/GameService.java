package com.czecht.tictactoe.application;

import javax.faces.application.FacesMessage;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.czecht.tictactoe.domain.game.Game;
import com.czecht.tictactoe.domain.game.GameStatus;
import com.czecht.tictactoe.domain.game.Movement;
import com.czecht.tictactoe.domain.history.GameHistory;
import com.czecht.tictactoe.domain.history.GameHistoryRepository;
import com.czecht.tictactoe.infrastructure.push.PushChannel;
import com.czecht.tictactoe.infrastructure.storage.GameStorage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GameService extends AbstractService {

	private final GameStorage gameStorage;

	private final PlayerService playerService;

	private final GameHistoryRepository gameHistoryRepository;

	@Autowired
	public GameService(GameStorage gameStorage, PlayerService playerService,
			GameHistoryRepository gameHistoryRepository) {
		this.gameStorage = gameStorage;
		this.playerService = playerService;
		this.gameHistoryRepository = gameHistoryRepository;
	}

	public Game createGame(String player) {
		//todo check is avaliable
		boolean playerLogged = playerService.isPlayerLogged(player);
		boolean playerInGame = gameStorage.isPlayerInGame(player);

		if(!playerLogged || playerInGame) {
			return null;
		}

		Game game = Game.getInstance(player, playerService.getCurrentUser());
		gameStorage.addGame(game);

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

	@Transactional
	public void makeMove(Game game, Movement movement) {

		Game currentGame = gameStorage.findGameById(game.getId());
		if(!currentGame.isActive()) {
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

				GameHistory.GameHistoryBuilder historyBuilder = GameHistory.builder()
					.gameTime(new DateTime().minus(game.getStartDate().getMillis()).getMillis())
					.playerCircle(currentGame.getPlayerCircle())
					.playerCross(currentGame.getPlayerCross())
					.result(convertResult(game.checkGameStatus()))
					.winner(game.getWinner());

			String gameResult = currentGame.checkGameStatus().toString();
			String winner = currentGame.getWinner();
			String messageDetails = String.format("%s %s", gameResult, winner);
			FacesMessage message = new FacesMessage("Game end.", messageDetails);
			pushMessage(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayerCircle()), message);
			pushMessage(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayerCross()), message);

			gameHistoryRepository.save(historyBuilder.build());
			gameStorage.delete(currentGame);
		}
	}

	private GameHistory.GameResult convertResult(GameStatus gameStatus){
		if(GameStatus.WIN.equals(gameStatus)) {
			return GameHistory.GameResult.WIN;
		} else {
			return GameHistory.GameResult.DRAW;
		}
	}
}
