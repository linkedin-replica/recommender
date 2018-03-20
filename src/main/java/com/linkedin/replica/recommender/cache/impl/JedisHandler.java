package com.linkedin.replica.recommender.cache.impl;

import com.linkedin.replica.recommender.cache.Cache;
import com.linkedin.replica.recommender.cache.handlers.RecommendationHandler;
import com.linkedin.replica.recommender.utils.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.LinkedHashMap;

public class JedisHandler implements RecommendationHandler {
    private JedisPool cachepool;
    private Configuration configuration = Configuration.getInstance();
    private String CACHE_USERS = configuration.getRedisConfig("cache.users.name");
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
        Jedis cacheInstance = cachepool.getResource();
        String key = CACHE_JOBS + userId;
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
        Jedis cacheInstance = cachepool.getResource();
        String key = CACHE_ARTICLES + userId;

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
        Jedis cacheInstance = cachepool.getResource();
        String key = CACHE_USERS + userId;
        if(!cacheInstance.exists(key)){

        }
    }
}
