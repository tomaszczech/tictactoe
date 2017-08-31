package com.czecht.tictactoe.infrastructure.push;

public interface PushChannel {

	String BOARD_CHANNEL = "board/%s";
	String BOARD_MESSAGE_CHANNEL = "board/message/%s";

}
