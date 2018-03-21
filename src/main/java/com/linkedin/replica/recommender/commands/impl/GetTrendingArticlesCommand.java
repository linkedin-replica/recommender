package com.linkedin.replica.recommender.commands.impl;

import com.linkedin.replica.recommender.cache.handlers.RecommendationCacheHandler;
import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.database.handlers.RecommendationDatabaseHandler;
import com.linkedin.replica.recommender.models.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Implementation of GetTrendingArticles command functionality.
 */

public class GetTrendingArticlesCommand extends Command {

    private RecommendationDatabaseHandler recommendationDatabaseHandler;
    private RecommendationCacheHandler recommendationCacheHandler;

    public GetTrendingArticlesCommand(HashMap<String, Object> args) {
        super(args);
    }

    public Object execute() throws IOException {
        String userId = this.args.get("userId").toString();
        recommendationDatabaseHandler = (RecommendationDatabaseHandler) dbHandler;
        // call dbHandler to get trendingArticles and return results in the results map as key-value pair
        ArrayList<Article> articles = recommendationDatabaseHandler.getTrendingArticles(userId);
        boolean toBeCached = (boolean) this.args.get("toBeCached");
        if(toBeCached){
            recommendationCacheHandler = (RecommendationCacheHandler) cacheHandler;
            recommendationCacheHandler.saveRecommendedArticles(userId, articles);
        }
        return articles;
    }
}
