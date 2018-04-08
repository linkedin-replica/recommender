package com.linkedin.replica.recommender.models;

public class User implements Comparable<User> {

    private int mutualCount;
    private String userId;
    private String firstName;
    private String lastName;
    private String headline;
    private String imageURL;

    public User(String userId, String firstName, String lastName, String headline, String imageURL) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.headline = headline;
        this.imageURL = imageURL;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setMutualCount(int mutualCount) {
        this.mutualCount = mutualCount;
    }

    public int compareTo(User other) {
        if (this.mutualCount != other.mutualCount)
            return this.userId.compareTo(other.userId);
        return other.mutualCount - this.mutualCount;
    }
}
