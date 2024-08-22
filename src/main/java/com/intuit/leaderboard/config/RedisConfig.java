package com.intuit.leaderboard.config;

import com.intuit.leaderboard.models.dto.RedisScore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.jedis.host-name}")
    private String hostName;

    @Value("${spring.redis.jedis.port}")
    private int port;

//    @Bean
//    public RedisTemplate<String, PlayerScore> redisTemplate() {
//        RedisTemplate<String, PlayerScore> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        return template;
//    }
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory
                = new JedisConnectionFactory();
        jedisConFactory.setHostName(hostName);
        jedisConFactory.setPort(port);
        return jedisConFactory;
    }

    @Bean
    public RedisTemplate<String, RedisScore> redisTemplate() {
        RedisTemplate<String, RedisScore> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
