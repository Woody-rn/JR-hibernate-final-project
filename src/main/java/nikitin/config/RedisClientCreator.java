package nikitin.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

import java.util.Objects;

public class RedisClientCreator {
    private static RedisClient redisClient;

    public static RedisClient get() {
        if (Objects.isNull(redisClient)) {
            redisClient = RedisClient.create(RedisURI.create("redis_container", 6379));
            try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
                System.out.println("\nConnected to Redis\n");
            }
        }
        return redisClient;
    }

    private RedisClientCreator() {
    }
}
