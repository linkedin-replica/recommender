package com.linkedin.replica.recommender.commands.impl;

import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.database.handlers.RecommendationDatabaseHandler;
import com.linkedin.replica.recommender.models.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of GetTrendingArticles command functionality.
 */

public class GetTrendingArticlesCommand extends Command {

    private RecommendationDatabaseHandler recommendationDatabaseHandler;

    public GetTrendingArticlesCommand(HashMap<String, Object> args) {
        super(args);
    }

    public Object execute() throws IOException {
        validateArgs(new String[]{"userId"});

        String userId = this.args.get("userId").toString();
        recommendationDatabaseHandler = (RecommendationDatabaseHandler) dbHandler;
        // call dbHandler to get trendingArticles and return results in the results map as key-value pair
        ArrayList<Article> articles = recommendationDatabaseHandler.getTrendingArticles(userId);
        return articles;
    }
}
