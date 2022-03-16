package com.echoxxzhang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> t = new RedisTemplate<>();
        t.setConnectionFactory(factory);

        // 设置key的序列化方式
        t.setKeySerializer(RedisSerializer.string());

        // 设置value的序列化方式
        t.setValueSerializer(RedisSerializer.json());

        // 设置hash
        t.setHashKeySerializer(RedisSerializer.string());
        t.setHashValueSerializer(RedisSerializer.json());

        t.afterPropertiesSet();
        return t;
    }
}
