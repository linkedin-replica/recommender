package com.linkedin.replica.recommender.cache.handlers.impl;

import com.linkedin.replica.recommender.cache.Cache;
import com.linkedin.replica.recommender.cache.handlers.RecommendationHandler;
import com.linkedin.replica.recommender.utils.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class JedisHandler implements RecommendationHandler {
    private JedisPool cachepool;
    private Configuration configuration = Configuration.getInstance();
    private String CACHE_FRIENDS = configuration.getRedisConfig("cache.friends.name");
    private String CACHE_JOBS = configuration.getRedisConfig("cache.jobs.name");
    private String CACHE_ARTICLES = configuration.getRedisConfig("cache.articles.name");

    public JedisHandler() throws IOException {
        cachepool = Cache.getInstance().getRedisPool();
    }

    /**
     * Adds the list of recommended jobs to the cache
     * related to a specific user
     *
     * @param userId
     * @param jobs
     */
    @Override
    public void saveRecommendedJobs(String userId, LinkedHashMap<String, Object> jobs) {
        /**
         * Gets a new instance from the cachepool
         */
        Jedis cacheInstance = cachepool.getResource();
        /**
         * Key is cache.name:userId
         */
        String key = CACHE_JOBS + ":" + userId;
        /**
         * Set the jobs hashmap of the user
         */
        cacheInstance.hmset(key, (HashMap) jobs);
    }

    /**
     * Adds the list of recommended articles to the cache
     * related to a specific user
     *
     * @param userId
     * @param articles
     */
    @Override
    public void saveRecommendedArticles(String userId, LinkedHashMap<String, Object> articles) {
        /**
         * Gets a new instance from the cachepool
         */
        Jedis cacheInstance = cachepool.getResource();
        /**
         * Key is cache.name:userId
         */
        String key = CACHE_ARTICLES + ":" + userId;
        /**
         * Set the articles hashmap of the user
         */
        cacheInstance.hmset(key, (HashMap) articles);
    }

    /**
     * Adds the list of recommended friends to the cache
     * related to a specific user
     *
     * @param userId
     * @param friends
     */
    @Override
    public void saveRecommendedFriends(String userId, LinkedHashMap<String, Object> friends) {
        /**
         * Gets a new instance from the cachepool
         */
        Jedis cacheInstance = cachepool.getResource();
        /**
         * Key is cache.name:userId
         */
        String key = CACHE_FRIENDS + ":" + userId;
        /**
         * Set the friends hashmap of the user
         */
        cacheInstance.hmset(key, (HashMap) friends);
    }
}
