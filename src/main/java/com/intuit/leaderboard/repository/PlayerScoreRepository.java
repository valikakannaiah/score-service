package com.intuit.leaderboard.repository;

import com.intuit.leaderboard.models.entities.PlayerScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerScoreRepository extends JpaRepository<PlayerScore, UUID> {

    Optional<PlayerScore> findByPlayerId(String playerId);

    @Query(value = "SELECT * from player_score ORDER BY score DESC LIMIT ?1", nativeQuery = true)
    List<PlayerScore> findTopScores(int limit);

}
