package com.czecht.tictactoe.readmodel.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.czecht.tictactoe.ddd.annotations.application.FinderImpl;
import com.czecht.tictactoe.ddd.presentation.AbstractSqlFinder;
import com.czecht.tictactoe.ddd.presentation.AbstractSqlResultTransformer;
import com.czecht.tictactoe.readmodel.HistoryDto;
import com.czecht.tictactoe.readmodel.HistoryFinder;

@FinderImpl
public class SqlHistoryFinder extends AbstractSqlFinder implements HistoryFinder {

	@Override
	public List<HistoryDto> findAll() {
		return getSession().createSQLQuery(
				"select player_circle as playerCircle, player_cross as playerCross, winner, result, "
						+ "game_time as gameTime from HISTORY order by gameTime desc ")
				.addScalar("playerCircle")
				.addScalar("playerCross")
				.addScalar("winner")
				.addScalar("result")
				.addScalar("gameTime")
				.setResultTransformer(new GameHistoryTransformer())
				.list();
	}

	@Override
	public List<HistoryDto> findByPlayer(String player) {
		return getSession().createSQLQuery(
				"select player_circle as playerCircle, player_cross as playerCross, winner, result, "
						+ "game_time as gameTime from HISTORY where player_circle=:player or player_cross=:player order by gameTime desc ó")
				.addScalar("playerCircle")
				.addScalar("playerCross")
				.addScalar("winner")
				.addScalar("result")
				.addScalar("gameTime")
				.setParameter("player", player)
				.setResultTransformer(new GameHistoryTransformer())
				.list();
	}

	private class GameHistoryTransformer extends AbstractSqlResultTransformer<HistoryDto> {

		@Override
		protected HistoryDto transformObject() {
			HistoryDto.HistoryDtoBuilder builder = HistoryDto.builder();

			builder.playerCircle(getString("playerCircle"))
					.playerCross(getString("playerCross"))
					.winner(getString("winner"))
					.result(transformGameResult(getString("result")))
					.gameTime(getBigInteger("gameTime").longValue());

			return builder.build();
		}

		private String transformGameResult(String gameResult) {
			// todo use Policy, remember of SOLID -> open/close
			if("DRAW".equals(gameResult)) {
				return "Remis";
			}
			return StringUtils.EMPTY;
		}

		@Override
		public List transformList(List list) {
			return list;
		}
	}
}
