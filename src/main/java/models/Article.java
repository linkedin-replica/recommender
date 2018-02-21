package models;

public class Article {

    private int authorId;
    private int postId;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String miniText;
    private int peopleTalking;


    public Article(int authorId, int postId, String title, String authorFirstName, String authorLastName, String miniText, int peopleTalking){
        this.authorId = authorId;
        this.postId = postId;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.miniText = miniText;
        this.peopleTalking = peopleTalking;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getPostId() {
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




}
