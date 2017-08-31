package com.czecht.tictactoe.infrastructure.push;

/**
 * Created by czecht on 2017-08-30.
 */
public interface PushChannel {

	String BOARD_CHANNEL = "board/%s";
	String BOARD_MESSAGE_CHANNEL = "board/message/%s";

}
