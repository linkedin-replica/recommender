package com.linkedin.replica.recommender.commands.impl;

import com.linkedin.replica.recommender.cache.handlers.RecommendationCacheHandler;
import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.models.User;
import com.linkedin.replica.recommender.database.handlers.RecommendationDatabaseHandler;

import java.io.IOException;
import java.util.*;

public class GetRecommendedUsersCommand extends Command {
    private RecommendationDatabaseHandler recommendationDatabaseHandler;
    private RecommendationCacheHandler recommendationCacheHandler;

    public GetRecommendedUsersCommand(HashMap<String, String> args) {
        super(args);
    }

    /**
     * Execute the command of recommending users to a certain user
     * @return The output (if any) of the command
     */
    public LinkedHashMap<String, Object> execute() throws IOException {
        String userId = this.args.get("userId");
        recommendationDatabaseHandler = (RecommendationDatabaseHandler) dbHandler;
        TreeMap<User, Integer> friendsOfFriends = recommendFriendsOfFriends(userId);
        ArrayList<User> recommendedUsers = sortFriendsOfFriends(friendsOfFriends);
        LinkedHashMap<String, Object> results = new LinkedHashMap<String, Object>();
        results.put("result", recommendedUsers);

        Boolean toBeCached = Boolean.parseBoolean(this.args.get("toBeCached"));
        if(toBeCached){
            recommendationCacheHandler = (RecommendationCacheHandler) cacheHandler;
            recommendationCacheHandler.saveRecommendedFriends(userId, results);
        }
        return results;
    }

    /**
     * get all unique friends of friends with their occurrences
     *
     * @param userId: the user to get his friends of friends.
     * @return map of the users and their occurrences count
     * @throws IOException if the congif file is not found
     */
    public TreeMap<User, Integer> recommendFriendsOfFriends(String userId) throws IOException {
        ArrayList<User> friends = recommendationDatabaseHandler.getFriendsOfUser(userId);
        TreeMap<User, Integer> friendsOfFriends = new TreeMap<User, Integer>();

        for (User friend : friends) {
            String friendId = friend.getUserId();
            ArrayList<User> friendsOfFriend = recommendationDatabaseHandler.getFriendsOfUser(friendId);
            for (User friendOfFriend : friendsOfFriend) {
                if (friendOfFriend.getUserId().equals(userId))
                    continue;

                Integer freq = friendsOfFriends.get(friendOfFriend);
                if (freq == null) freq = 0;

                friendsOfFriends.put(friendOfFriend, freq + 1);
            }
        }
        return friendsOfFriends;
    }

    /**
     * sorts the recommended users based on the number of mutual friends
     *
     * @param friendsOfFriends: maps each user to the number of mutual friends
     * @return sorted list of the recommended users
     */
    private ArrayList<User> sortFriendsOfFriends(TreeMap<User, Integer> friendsOfFriends) {
        ArrayList<User> recommendedUsers = new ArrayList<User>();
        for (Map.Entry<User, Integer> entry : friendsOfFriends.entrySet()) {
            User cur = entry.getKey();
            cur.setMutualCount(entry.getValue());
            recommendedUsers.add(cur);
        }

        Collections.sort(recommendedUsers);
        return recommendedUsers;
    }
}
