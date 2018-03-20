package com.linkedin.replica.recommender.commands.impl;

import com.linkedin.replica.recommender.cache.handlers.RecommendationCacheHandler;
import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.database.handlers.RecommendationDatabaseHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Implementation of GetTrendingArticles command functionality.
 */

public class GetTrendingArticlesCommand extends Command {

    private RecommendationDatabaseHandler recommendationDatabaseHandler;
    private RecommendationCacheHandler recommendationCacheHandler;

    public GetTrendingArticlesCommand(HashMap<String, String> args) {
        super(args);
    }

    public LinkedHashMap execute() throws IOException {
        LinkedHashMap<String, Object> results = new LinkedHashMap<>();
        String userId = this.args.get("userId");
        recommendationDatabaseHandler = (RecommendationDatabaseHandler) dbHandler;
        // call dbHandler to get trendingArticles and return results in the results map as key-value pair
        results.put("results", recommendationDatabaseHandler.getTrendingArticles(userId));
        Boolean toBeCached = Boolean.parseBoolean(this.args.get("toBeCached"));
        if(toBeCached){
            recommendationCacheHandler = (RecommendationCacheHandler) cacheHandler;
            recommendationCacheHandler.saveRecommendedArticles(userId, results);
        }
        return results;
    }
}
