package com.linkedin.replica.recommender.cache.handlers.impl;

import com.google.gson.Gson;
import com.linkedin.replica.recommender.cache.Cache;
import com.linkedin.replica.recommender.cache.handlers.RecommendationCacheHandler;
import com.linkedin.replica.recommender.utils.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class JedisCacheHandler implements RecommendationCacheHandler {
    private static Gson gson;
    private static JedisPool cachePool;
    private Configuration configuration = Configuration.getInstance();
    private String CACHE_FRIENDS = configuration.getRedisConfig("cache.friends.name");
    private String CACHE_JOBS = configuration.getRedisConfig("cache.jobs.name");
    private String CACHE_ARTICLES = configuration.getRedisConfig("cache.articles.name");

    public JedisCacheHandler() throws IOException {
        cachePool = Cache.getInstance().getRedisPool();
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
    public void saveRecommendedJobs(String userId, Object jobs) throws IOException {
        String key = CACHE_JOBS + ":" + userId;
        addList(key, jobs);
    }

    /**
     * Adds the list of recommended articles to the cache
     * related to a specific user
     *
     * @param userId
     * @param articles
     */
    @Override
    public void saveRecommendedArticles(String userId, Object articles) throws IOException {
        String key = CACHE_ARTICLES + ":" + userId;
        addList(key, articles);
    }

    /**
     * Adds the list of recommended friends to the cache
     * related to a specific user
     *
     * @param userId
     * @param friends
     */
    @Override
    public void saveRecommendedFriends(String userId, Object friends) throws IOException {
        String key = CACHE_FRIENDS + ":" + userId;
        addList(key, friends);
    }

    private static void addList(String key, Object objectsList) throws IOException {
        try {
            Jedis cacheInstance = cachePool.getResource();
            Pipeline pipeline = cacheInstance.pipelined();
            ArrayList<Object> objects = (ArrayList<Object>) objectsList;
            for(Object object : objects) {
                String jsonObj = gson.toJson(object);
                pipeline.sadd(key, jsonObj);
            }
            pipeline.sync();
            pipeline.close();
            cacheInstance.close();
        } catch (JedisException e) {
            e.printStackTrace();
            return;
        }
    }

    public static <T> ArrayList<T> getTList(String key, Class<T> tClass) {
        try {
            Jedis cacheInstance = cachePool.getResource();
            ArrayList<T> list = new ArrayList();
            Set<String> cacheResults = cacheInstance.smembers(key);
            for (String result : cacheResults) {
                T object = gson.fromJson(result, tClass);
                list.add(object);
            }
            cacheInstance.close();
            return list;
        } catch(JedisException e) {
            e.printStackTrace();
        }
        return null;
    }

}
