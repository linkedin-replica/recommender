package com.linkedin.replica.recommender.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.linkedin.replica.recommender.utils.Configuration;

import java.io.IOException;

public class Cache {
    private Configuration configuration = Configuration.getInstance();
    private final String REDIS_IP = configuration.getAppConfig("redis.ip");
    private final int REDIS_PORT = Integer.parseInt(configuration.getAppConfig("redis.port"));
    private JedisPool redisPool;
    private static Cache cache;

    private Cache() throws IOException {
        redisPool = new JedisPool(new JedisPoolConfig(), REDIS_IP, REDIS_PORT);
    }

    /**
     * Get a singleton cache instance
     *
     * @return The cache instance
     */
    public static Cache getInstance() throws IOException {
        if (cache == null) {
            synchronized (Cache.class) {
                if (cache == null)
                    cache = new Cache();
            }
        }
        return cache;
    }

    public JedisPool getRedisPool() {
        return redisPool;
    }

    /** Destroys pool */
    public void destroyRedisPool(){
        redisPool.destroy();
    }
}
