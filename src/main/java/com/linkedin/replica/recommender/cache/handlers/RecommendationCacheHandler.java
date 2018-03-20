package com.linkedin.replica.recommender.cache.handlers;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface RecommendationCacheHandler extends CacheHandler {


    /**
     * Adds the list of recommended jobs to the cache
     * related to a specific user
     */
    void saveRecommendedJobs(String userId, Object jobs);

    /**
     * Adds the list of recommended articles to the cache
     * related to a specific user
     */
    void saveRecommendedArticles(String userId, Object articles);

    /**
     * Adds the list of recommended friends to the cache
     * related to a specific user
     */
    void saveRecommendedFriends(String userId, Object friends);
}
