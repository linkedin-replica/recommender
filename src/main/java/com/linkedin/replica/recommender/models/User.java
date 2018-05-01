package com.linkedin.replica.recommender.models;

public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String headline;
    private String industry;
    private String profilePictureUrl;

    public User() {
        super();
    }

    public String getUserId() {
        return this.userId;
    }

}
