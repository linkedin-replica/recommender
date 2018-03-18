package com.linkedin.replica.recommender.models;

public class Article {

    private String postId;
    private String authorId;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String miniText;
    private int peopleTalking;


    public Article() {
        super();
    }

    public Article(String authorId, String postId, String title, String authorFirstName, String authorLastName, String miniText, int peopleTalking) {
        this();
        this.authorId = authorId;
        this.postId = postId;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.title = title;
        this.miniText = miniText;
        this.peopleTalking = peopleTalking;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public String getMiniText() {
        return miniText;
    }

    public int getPeopleTalking() {
        return peopleTalking;
    }

    @Override
    public String toString() {
        return title + "\t" + authorFirstName + " " + authorLastName + " " + peopleTalking;
    }
}
