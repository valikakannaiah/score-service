package com.intuit.leaderboard.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Table(name = "player_score")
@Data
@Entity
public class PlayerScore {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "player_score")
    private int playerScore;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private String createdBy;

}
