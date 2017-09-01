package com.czecht.tictactoe.application;

import javax.faces.application.FacesMessage;

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

public abstract class AbstractService {

	public void pushMessage(String channel, String message) {
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish(channel, message);
	}

	public void pushMessage(String channel, FacesMessage message) {
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish(channel, message);
	}
}
