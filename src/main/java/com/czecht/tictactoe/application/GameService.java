package com.czecht.tictactoe.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.faces.application.FacesMessage;

import org.joda.time.DateTime;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
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

@Service
public class GameService {

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
		Game game = Game.getInstance(player, playerService.getCurrentUser());
		gameStorage.addGame(game);
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish(player, game.getId());
		return game;
	}

	public Game createRandomGame() {
		Set<String> players = new HashSet<>(playerService.findAvailablePlayersForGame());
		players.remove(playerService.getCurrentUser());

		List<String> listToRandomPlayer = new ArrayList<>();
		listToRandomPlayer.addAll(players);

		int randomIndex=0;
		if(players.size()>1) {
			randomIndex = ThreadLocalRandom.current().nextInt(0, players.size() - 1);
		}

		String randomPlayer = listToRandomPlayer.get(randomIndex);

		Game game = Game.getInstance(randomPlayer, playerService.getCurrentUser());
		gameStorage.addGame(game);

		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish(randomPlayer, game.getId());
		return game;
	}



	public Game findGameById(Long gameId) {
		return gameStorage.findGameById(gameId);
	}

	@Transactional
	public void makeMove(Game game, Movement movement) {

		Game currentGame = gameStorage.findGameById(game.getId());
		currentGame.makeMove(movement);
		GameStatus gameStatus = currentGame.checkGameStatus();

		EventBus eventBus = EventBusFactory.getDefault().eventBus();

		if(GameStatus.CONTINOUE.equals(gameStatus)) {
			eventBus.publish(String.format(PushChannel.BOARD_CHANNEL, currentGame.getPlayer0()), game.getId());
			eventBus.publish(String.format(PushChannel.BOARD_CHANNEL, currentGame.getPlayer1()), game.getId());
		} else {
			GameHistory.GameHistoryBuilder historyBuilder = GameHistory.builder()
					.gameTime(new DateTime().minus(game.getStartDate().getMillis()).getMillis())
					.playerCircle(currentGame.getPlayerO())
					.playerCross(currentGame.getPlayerX());
			if(GameStatus.WIN.equals(gameStatus)) {
				historyBuilder.result(GameHistory.GameResult.WIN).winner(movement.getPlayer());

				String status = "Wygral: " + movement.getPlayer();
				FacesMessage message = new FacesMessage("Koniec.", status);
				eventBus.publish(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayer0()), message);
				eventBus.publish(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayer1()), message);
			} else {
				historyBuilder.result(GameHistory.GameResult.DRAW);

				String status = "Remis";
				FacesMessage message = new FacesMessage("Koniec.", status);
				eventBus.publish(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayer0()), message);
				eventBus.publish(String.format(PushChannel.BOARD_MESSAGE_CHANNEL, currentGame.getPlayer1()), message);
			}

			gameHistoryRepository.save(historyBuilder.build());
			gameStorage.delete(currentGame);
		}
	}
}
