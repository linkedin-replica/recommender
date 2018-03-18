package com.linkedin.replica.recommender.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.linkedin.replica.recommender.utils.Configuration;

import java.io.IOException;

public class Cache {
    private Configuration configuration = Configuration.getInstance();
    private final String REDIS_IP = configuration.getAppConfig("redis.ip");
    private final int REDIS_PORT = Integer.parseInt(configuration.getAppConfig("redis.port"));
    protected static JedisPool redisPool;

    public Cache() throws IOException {
        redisPool = new JedisPool(new JedisPoolConfig(), REDIS_IP, REDIS_PORT);
    }
}
