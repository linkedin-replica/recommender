package com.linkedin.replica.recommender.cache.impl;

import com.linkedin.replica.recommender.cache.Cache;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;

public class RecommendationCache extends Cache{

    public static Jedis recommendationCache = redisPool.getResource();
    /***
     *   Pipeline allows execution of multiple operations in 1 request to Redis for saving network latencies
     */
    private static Pipeline recommendationPipeline = recommendationCache.pipelined();

    public RecommendationCache() throws IOException {
    }

}
