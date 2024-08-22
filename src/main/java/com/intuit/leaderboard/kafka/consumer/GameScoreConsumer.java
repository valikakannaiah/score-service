package com.intuit.leaderboard.kafka.consumer;

import com.intuit.leaderboard.exceptions.LeaderBoardCustomException;
import com.intuit.leaderboard.models.entities.PlayerScore;
import com.intuit.leaderboard.models.dto.RedisScore;
import com.intuit.leaderboard.models.entities.kafka.KafkaPlayerScoreEvent;
import com.intuit.leaderboard.repository.PlayerScoreRepository;
import com.intuit.leaderboard.service.mappers.PlayerScoreMapper;
import com.intuit.leaderboard.service.utils.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameScoreConsumer {

    private final CacheUtil cacheUtil;
    private final PlayerScoreRepository playerScoreRepository;
    private final PlayerScoreMapper playerScoreMapper;

    @KafkaListener(topics = "${spring.kafka.topic-player-score.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void gameScorePgConsumer(ConsumerRecord<String, KafkaPlayerScoreEvent> record, Acknowledgment acknowledgment){

        try{
            KafkaPlayerScoreEvent kafkaPlayerScoreEvent = record.value();
            //can also validate the score if value being read is of proper format or not
            PlayerScore playerScore = playerScoreMapper.mapToEntity(kafkaPlayerScoreEvent);
            RedisScore redisScore = playerScoreMapper.maptToRedisScore(playerScore);
            PlayerScore playerScoreEntity = saveToDB(playerScore);
            int score = playerScoreEntity.getPlayerScore();
            cacheUtil.addToCache(redisScore, score);
            acknowledgment.acknowledge();
            log.debug("record saved to cache , key: {}", record.key());
        }catch(LeaderBoardCustomException exp){
            throw exp;
        }
        catch(Exception e){
            throw new LeaderBoardCustomException(HttpStatus.BAD_REQUEST, List.of(e.getMessage()));
        }
    }

    private PlayerScore saveToDB(PlayerScore playerScore) {
        try {
            PlayerScore playerScoreEntity = playerScoreRepository.findByPlayerId(playerScore.getPlayerId())
                    .map(existingPlayerScore -> {
                        // If present, update the existing record with the new score
                        existingPlayerScore.setPlayerScore(playerScore.getPlayerScore() + existingPlayerScore.getPlayerScore());
                        existingPlayerScore.setCreatedAt(new Date());
                        return playerScoreRepository.save(existingPlayerScore);
                    })
                    .orElseGet(() -> {
                        // If not present, save the new player score
                        playerScore.setCreatedAt(new Date());
                        return playerScoreRepository.save(playerScore);
                    });
            return playerScoreEntity;
        } catch (Exception e) {
            log.error("Error occured while saving score to database");
            throw new LeaderBoardCustomException(HttpStatus.BAD_REQUEST, List.of(e.getMessage()));
        }
    }
}
