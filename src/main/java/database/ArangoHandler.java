package database;

import models.Article;
import models.JobListing;
import models.User;

import java.util.ArrayList;

public class ArangoHandler implements DatabaseHandler {
    /**
     * Initiate a connection with the database
     */
    public void connect() {

    }

    /**
     * Get the recommended users for a specific user
     *
     * @param userId : the user seeking friend recommendations
     * @return list of recommended users
     */
    public ArrayList<User> getRecommendedUsers(int userId) {
        return null;
    }

    /**
     * Get the recommended job listings for a specific user
     *
     * @param userId : the user seeking job listing recommendations
     * @return list of recommended job listings
     */
    public ArrayList<JobListing> getRecommendedJobListing(int userId) {
        return null;
    }

    /**
     * Get the trending articles for a specific user
     *
     * @param userId : the user seeking trending articles
     * @return list of trending articles
     */
    public ArrayList<Article> getTrendingArticles(int userId) {
        return null;
    }

    /**
     * Close a connection with the database
     */
    public void disconnect() {

    }
}
