package com.czecht.tictactoe.ddd.model;

public interface EventListener<E extends Event> {

	void listen(E event);

}