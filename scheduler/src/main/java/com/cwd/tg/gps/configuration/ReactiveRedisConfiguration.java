package com.cwd.tg.gps.configuration;

import com.cwd.tg.gps.security.UserToken;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;

@Configuration
@AllArgsConstructor
public class ReactiveRedisConfiguration {

    private final ReactiveRedisConnectionFactory factory;

    @PostConstruct
    public void clearData() {
        factory.getReactiveConnection().serverCommands().flushAll()
                .subscribe(s -> System.out.println("Database was cleaned"));
    }

    @Bean
    ReactiveRedisOperations<String, UserToken> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<UserToken> serializer = new Jackson2JsonRedisSerializer<>(UserToken.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, UserToken> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, UserToken> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
