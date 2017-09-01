package com.czecht.tictactoe.infrastructure.push;

import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PushEndpoint("{user}")
@Singleton
public class NotifyResource {

	@PathParam("user")
	private String user;

	@OnMessage(encoders = { JSONEncoder.class })
	public String onMessage(String newGameId) {
		log.info(String.format("new game[%s]", newGameId));
		return newGameId;
	}

	@OnOpen
	public void onOpen(RemoteEndpoint r, EventBus eventBus) {
		log.info("PushResource.onOpen endpoint: {0}", new Object[] { r });
	}

	@OnClose
	public void onClose(RemoteEndpoint r, EventBus eventBus) {
		log.info("PushResource.onClose endpoint: {0}", new Object[] { r });
	}

}
