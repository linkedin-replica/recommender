package com.linkedin.replica.recommender.commands.impl;

import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.database.handlers.RecommendationHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Implementation of GetTrendingArticles command functionality.
 */

public class GetTrendingArticlesCommand extends Command {

    public GetTrendingArticlesCommand(HashMap<String, String> args) {
        super(args);
    }

    public LinkedHashMap execute() throws IOException {
        LinkedHashMap<String, Object> results = new LinkedHashMap<>();
        RecommendationHandler recommendationHandler = (RecommendationHandler) dbHandler;
        // call dbHandler to get trendingArticles and return results in the results map as key-value pair
        results.put("results", recommendationHandler.getTrendingArticles(this.args.get("userId")));
        return results;
    }
}
