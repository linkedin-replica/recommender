package recommender;

import models.Command;
import models.User;

import java.util.*;

public class getRecommendedUsersCommand extends Command {

    public getRecommendedUsersCommand(HashMap<String, String> args) {
        super(args);
    }

    /**
     * Execute the command
     *
     * @return The output (if any) of the command
     */
    public LinkedHashMap<String, Object> execute() {
        String userId = this.args.get("userId");
        TreeMap<User, Integer> friendsOfFriends = recommendFriendsOfFriends(userId);
        ArrayList<User> recommendedUsers = sortFriendsOfFriends(friendsOfFriends);
        LinkedHashMap<String, Object> results = new LinkedHashMap<String, Object>();
        results.put("result", recommendedUsers);
        return results;
    }

    public TreeMap<User, Integer> recommendFriendsOfFriends(String userId) {
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
    public ArrayList<User> sortFriendsOfFriends(TreeMap<User, Integer> friendsOfFriends) {
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
