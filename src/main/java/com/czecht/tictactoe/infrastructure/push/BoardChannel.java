package com.czecht.tictactoe.infrastructure.push;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PushEndpoint("/board/{user}")
@Singleton
public class BoardChannel {

	@PathParam("user")
	private String user;

	@OnMessage(encoders = { JSONEncoder.class })
	public String onMessage(String gameId) {
		log.info(String.format("move on game[%s]", gameId));
		return gameId;
	}

}
