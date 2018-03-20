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
import java.util.Set;

public class JedisCacheHandler implements RecommendationCacheHandler {
    private static Gson gson;
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
        ArrayList<Object> jobListings = (ArrayList<Object>) jobs;
        addList(jobListings, key, cacheInstance);
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
        ArrayList<Object> articlesList = (ArrayList<Object>) articles;
        addList(articlesList, key, cacheInstance);
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
        ArrayList<Object> friendsList = (ArrayList<Object>) friends;
        addList(friendsList, key, cacheInstance);
    }

    private static void addList(ArrayList<Object> objectsList, String key, Jedis cacheInstance) {
        for(Object object : objectsList) {
            String jsonObj = gson.toJson(object);
            cacheInstance.sadd(key, jsonObj);
        }
    }

    public static ArrayList<JobListing> getJobsList(String key, Jedis cacheInstance) {
        ArrayList<JobListing> jobListings = new ArrayList<JobListing>();
        Set<String> cacheResults = cacheInstance.smembers(key);
        for (String result : cacheResults) {
            JobListing jobListing = gson.fromJson(result, JobListing.class);
            jobListings.add(jobListing);
        }
        return jobListings;
    }

    public static ArrayList<Article> getArticlesList(String key, Jedis cacheInstance) {
        ArrayList<Article> articles = new ArrayList<Article>();
        Set<String> cacheResults = cacheInstance.smembers(key);
        for (String result : cacheResults) {
            Article cachedArticle = gson.fromJson(result, Article.class);
            articles.add(cachedArticle);
        }
        return articles;
    }

    public static ArrayList<User> getUsersList(String key, Jedis cacheInstance) {
        ArrayList<User> users = new ArrayList<User>();
        Set<String> cacheResults = cacheInstance.smembers(key);
        for (String result : cacheResults) {
            User cachedUser = gson.fromJson(result, User.class);
            users.add(cachedUser);
        }
        return users;
    }

}
