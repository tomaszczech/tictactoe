package com.czecht.tictactoe.infrastructure.push;

import javax.faces.application.FacesMessage;

import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PushEndpoint("/board/message/{user}")
@Singleton
public class BoardMessageChannel {

	@PathParam("user")
	private String user;

	@OnMessage(encoders = { JSONEncoder.class })
	public FacesMessage onMessage(FacesMessage message) {
		log.info(String.format("move user[%s]", message));
		return message;
	}

}
