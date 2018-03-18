package com.linkedin.replica.recommender.models;

public class User implements Comparable<User> {

    private int mutualCount;
    private String userId;
    private String firstName;
    private String lastName;
    private String headline;
    private String industry;

    public User(String userId, String firstName, String lastName, String headline, String industry) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.headline = headline;
        this.industry = industry;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getHeadline() {
        return this.headline;
    }

    public String getIndustry() {
        return this.industry;
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
