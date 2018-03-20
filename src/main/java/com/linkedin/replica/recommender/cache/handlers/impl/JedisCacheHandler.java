package com.linkedin.replica.recommender.cache.handlers.impl;

import com.google.gson.Gson;
import com.linkedin.replica.recommender.cache.Cache;
import com.linkedin.replica.recommender.cache.handlers.RecommendationCacheHandler;
import com.linkedin.replica.recommender.models.Article;
import com.linkedin.replica.recommender.models.JobListing;
import com.linkedin.replica.recommender.models.User;
import com.linkedin.replica.recommender.utils.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class JedisCacheHandler implements RecommendationCacheHandler {
    private Gson gson;
    private JedisPool cachepool;
    private Configuration configuration = Configuration.getInstance();
    private String CACHE_FRIENDS = configuration.getRedisConfig("cache.friends.name");
    private String CACHE_JOBS = configuration.getRedisConfig("cache.jobs.name");
    private String CACHE_ARTICLES = configuration.getRedisConfig("cache.articles.name");

    public JedisCacheHandler() throws IOException {
        cachepool = Cache.getInstance().getRedisPool();
        gson = new Gson();
    }

    /**
     * Adds the list of recommended jobs to the cache
     * related to a specific user
     *
     * @param userId
     * @param jobs
     */
    @Override
    public void saveRecommendedJobs(String userId, Object jobs) {
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
        ArrayList<JobListing> jobListings = (ArrayList<JobListing>) jobs;
        for (JobListing job:jobListings) {
            String jsonObj = gson.toJson(job);
            cacheInstance.sadd(key, jsonObj);
        }
    }

    /**
     * Adds the list of recommended articles to the cache
     * related to a specific user
     *
     * @param userId
     * @param articles
     */
    @Override
    public void saveRecommendedArticles(String userId, Object articles) {
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
        ArrayList<Article> articlesList = (ArrayList<Article>) articles;
        for (Article article: articlesList) {
            String jsonObj = gson.toJson(article);
            cacheInstance.sadd(key, jsonObj);
        }
    }

    /**
     * Adds the list of recommended friends to the cache
     * related to a specific user
     *
     * @param userId
     * @param friends
     */
    @Override
    public void saveRecommendedFriends(String userId, Object friends) {
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
        ArrayList<User> friendsList = (ArrayList<User>) friends;
        for (User user: friendsList) {
            String jsonObj = gson.toJson(user);
            cacheInstance.sadd(key, jsonObj);
        }
    }
}
