package com.linkedin.replica.recommender.commands.impl;

import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.database.handlers.RecommendationDatabaseHandler;
import com.linkedin.replica.recommender.models.JobListing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Implementation of RecommendJobListing command functionality.
 */

public class RecommendJobListingCommand extends Command {

    private RecommendationDatabaseHandler recommendationDatabaseHandler;

    public RecommendJobListingCommand(HashMap<String, Object> args) {
        super(args);
    }

    @Override
    public Object execute() throws IOException {
        validateArgs(new String[]{"userId"});

        String userId = this.args.get("userId").toString();
        recommendationDatabaseHandler = (RecommendationDatabaseHandler) dbHandler;
        // call dbHandler to get recommendedJobs and return results in the results map as key-value pair
        ArrayList<JobListing> jobListings = recommendationDatabaseHandler.getRecommendedJobListing(userId);


        return jobListings;
    }
}
