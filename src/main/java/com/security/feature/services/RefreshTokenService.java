package com.security.feature.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    public String createRefreshToken(Long userId){
        String token = UUID.randomUUID().toString();
        String key = REFRESH_TOKEN_PREFIX + token;

        redisTemplate.opsForValue().set(key, userId.toString(), Duration.ofDays(7));
        return token;
    }

    public String validateRefreshToken(String token){
        String key = REFRESH_TOKEN_PREFIX + token;
        String userId = redisTemplate.opsForValue().get(key);

        if(userId.isEmpty()){
            throw new RuntimeException("Invalid or Expired refresh Token");
        }

        return userId;
    }

    public void deleteRefreshToken(String token){
        String key = REFRESH_TOKEN_PREFIX + token;
        redisTemplate.delete(key);
    }

}
