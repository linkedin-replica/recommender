package com.linkedin.replica.recommender.commands.impl;

import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.models.User;
import com.linkedin.replica.recommender.database.handlers.RecommendationDatabaseHandler;

import java.io.IOException;
import java.util.*;

public class GetRecommendedUsersCommand extends Command {
    private RecommendationDatabaseHandler recommendationDatabaseHandler;

    public GetRecommendedUsersCommand(HashMap<String, Object> args) {
        super(args);
    }

    /**
     * Execute the command of recommending users to a certain user
     * @return The output (if any) of the command
     */
    public Object execute() throws IOException {
        validateArgs(new String[]{"userId"});
        String userId = this.args.get("userId").toString();
        recommendationDatabaseHandler = (RecommendationDatabaseHandler) dbHandler;
        ArrayList<User> recommendedUsers = recommendationDatabaseHandler.getFriendsOfFriends(userId);


        return recommendedUsers;
    }

}
