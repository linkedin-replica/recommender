package com.linkedin.replica.recommender.models;

import java.util.ArrayList;

public class Article {

    private String authorId;
    private String postId;
    private int commentsCount;
    private long timestamp;
    private boolean isCompanyPost;
    private boolean liked;
    private String authorName;
    private String authorProfilePictureUrl;
    private String headline;
    private int peopleTalking;
    private String miniText;
    private String title;

    public String getAuthorId() {
        return authorId;
    }

    public String getPostId() {
        return postId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorProfilePictureUrl() {
        return authorProfilePictureUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public int getPeopleTalking() {
        return peopleTalking;
    }

    public String getMiniText() {
        return miniText;
    }

    public Article() {
        super();
    }

    public String getTitle() {
        return this.title;
    }
}
