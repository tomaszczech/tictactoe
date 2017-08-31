package com.czecht.tictactoe.readmodel;

import java.util.List;

import com.czecht.tictactoe.ddd.annotations.application.Finder;

@Finder
public interface HistoryFinder {

	List<HistoryDto> findAll();

	List<HistoryDto> findByPlayer(String player);
}
