package com.czecht.tictactoe.domain.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameHistoryRepository extends JpaRepository<GameHistory, Long> {

	@Query("select gh from GameHistory gh where gh.playerCircle = :player or gh.playerCross = :player")
	List<GameHistory> findByPayer(@Param("player") String player);
}
