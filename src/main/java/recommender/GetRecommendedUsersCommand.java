package recommender;

import models.Command;
import models.User;

import java.io.IOException;
import java.util.*;

public class GetRecommendedUsersCommand extends Command {

    public GetRecommendedUsersCommand(HashMap<String, String> args) {
        super(args);
    }

    /**
     * Execute the command of recommending users to a certain user
     * @return The output (if any) of the command
     */
    public LinkedHashMap<String, Object> execute() throws IOException {
        String userId = this.args.get("userId");
        TreeMap<User, Integer> friendsOfFriends = recommendFriendsOfFriends(userId);
        ArrayList<User> recommendedUsers = sortFriendsOfFriends(friendsOfFriends);
        LinkedHashMap<String, Object> results = new LinkedHashMap<String, Object>();
        results.put("result", recommendedUsers);
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
        ArrayList<User> friends = this.dbHandler.getFriendsOfUser(userId);
        TreeMap<User, Integer> friendsOfFriends = new TreeMap<User, Integer>();

        for (User friend : friends) {
            String friendId = friend.getUserId();
            ArrayList<User> friendsOfFriend = this.dbHandler.getFriendsOfUser(friendId);
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
