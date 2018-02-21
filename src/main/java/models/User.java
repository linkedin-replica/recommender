package models;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String headline;
    private String industry;

    public User(int userId, String firstName, String lastName, String headline, String industry) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.headline = headline;
        this.industry = industry;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHeadline() {
        return headline;
    }

    public String getIndustry() {
        return industry;
    }

}
