package com.linkedin.replica.recommender.commands.impl;

import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.database.handlers.RecommendationHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *  Implementation of RecommendJobListing command functionality.
 */

public class RecommendJobListingCommand extends Command {

    public RecommendJobListingCommand(HashMap<String, String> args) {
        super(args);
    }

    @Override
    public LinkedHashMap<String, Object> execute() throws IOException {
        LinkedHashMap<String, Object> results = new LinkedHashMap<>();
        RecommendationHandler recommendationHandler = (RecommendationHandler) dbHandler;
        // call dbHandler to get recommendedJobs and return results in the results map as key-value pair
        results.put("results", recommendationHandler.getRecommendedJobListing(this.args.get("userId")));
        return results;
    }
}
