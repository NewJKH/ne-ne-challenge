package com.github.nenidan.ne_ne_challenge.domain.admin.infrastructure.respository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.github.nenidan.ne_ne_challenge.domain.admin.domain.repository.StatisticsRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StatisticsRedisRepositoryImpl implements StatisticsRedisRepository {

    private static final String PREFIX = "statistics:";
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String type, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(PREFIX + type);
        return clazz.cast(value);
    }

}
