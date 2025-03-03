package com.security.feature.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean isAllowed(String key, int maxRequests, long durationInSeconds){
        long currentRequests = redisTemplate.opsForValue().increment(key, 1);
        if(currentRequests == 1){
            redisTemplate.expire(key, durationInSeconds, TimeUnit.SECONDS);
        }
        return currentRequests <= maxRequests;
    }
}
