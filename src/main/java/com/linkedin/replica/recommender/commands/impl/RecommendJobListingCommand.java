package com.linkedin.replica.recommender.commands.impl;

import com.linkedin.replica.recommender.cache.handlers.RecommendationCacheHandler;
import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.database.handlers.RecommendationDatabaseHandler;
import com.linkedin.replica.recommender.models.JobListing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *  Implementation of RecommendJobListing command functionality.
 */

public class RecommendJobListingCommand extends Command {

    private RecommendationDatabaseHandler recommendationDatabaseHandler;
    private RecommendationCacheHandler recommendationCacheHandler;

    public RecommendJobListingCommand(HashMap<String, String> args) {
        super(args);
    }

    @Override
    public Object execute() throws IOException {
        LinkedHashMap<String, Object> results = new LinkedHashMap<>();
        String userId = this.args.get("userId");
        recommendationDatabaseHandler = (RecommendationDatabaseHandler) dbHandler;
        // call dbHandler to get recommendedJobs and return results in the results map as key-value pair
        ArrayList<JobListing> jobListings = recommendationDatabaseHandler.getRecommendedJobListing(userId);
        Boolean toBeCached = Boolean.parseBoolean(this.args.get("toBeCached"));

        if(toBeCached){
            recommendationCacheHandler = (RecommendationCacheHandler) cacheHandler;
            recommendationCacheHandler.saveRecommendedJobs(userId, jobListings);
        }
        return jobListings;
    }
}
