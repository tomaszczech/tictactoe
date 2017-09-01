package com.czecht.tictactoe;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.czecht.tictactoe.domain.game.Board;
import com.czecht.tictactoe.domain.game.GameStatus;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class BoardTest {

	@DataProvider
	public Object[][] winningBoards() {
		return new Object[][] {
				{ new BoardBuilder().line1(1, 0, null).line2(1, 0, null).line3(1, null, null).build() } };
	}

	@Test(dataProvider = "winningBoards")
	public void testWinningBoard(Board board) {
		// given

		// when
		GameStatus gameStatus = board.checkBoardState(2, 0, 1);

		// then
		assertThat(gameStatus).isEqualTo(GameStatus.WIN);
	}

	@DataProvider
	public Object[][] notWinningBoards() {
		return new Object[][] { { new BoardBuilder().line1(1, 0, null).line2(1, 0, null).build() } };
	}

	@Test(dataProvider = "notWinningBoards")
	public void testNotWinningBoard(Board board) {
		// given

		// when
		GameStatus gameStatus = board.checkBoardState(0, 1, 1);

		// then
		assertThat(gameStatus).isEqualTo(GameStatus.CONTINOUE);
	}

	private static class BoardBuilder {

		private Integer[][] board = new Integer[3][3];

		public BoardBuilder line1(Integer x, Integer y, Integer z) {
			line(0, x, y, z);
			return this;
		}

		public BoardBuilder line2(Integer x, Integer y, Integer z) {
			line(1, x, y, z);
			return this;
		}

		public BoardBuilder line3(Integer x, Integer y, Integer z) {
			line(2, x, y, z);
			return this;
		}

		private BoardBuilder line(int row, Integer x, Integer y, Integer z) {
			board[row][0] = x;
			board[row][1] = y;
			board[row][2] = z;
			return this;
		}

		public Board build() {
			return new Board(board);
		}
	}
}
