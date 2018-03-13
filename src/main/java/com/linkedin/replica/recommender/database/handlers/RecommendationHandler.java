package com.linkedin.replica.recommender.database.handlers;

import com.arangodb.ArangoCursor;
import com.arangodb.velocypack.VPackSlice;
import com.linkedin.replica.recommender.models.Article;
import com.linkedin.replica.recommender.models.JobListing;
import com.linkedin.replica.recommender.models.User;

import java.io.IOException;
import java.util.ArrayList;

public interface RecommendationHandler extends DatabaseHandler{

    /**
     * Get the recommended users for a specific user
     *
     * @param userId: the user seeking friend recommendations
     * @return list of recommended users
     */
    ArrayList<User> getFriendsOfUser(String userId) throws IOException;


    /**
     * Get the recommended job listings for a specific user
     *
     * @param userId: the user seeking job listing recommendations
     * @return list of recommended job listings
     */
    ArrayList<JobListing> getRecommendedJobListing(String userId) throws IOException;

    /**
     * Get a user by his id
     *
     * @param userId: the user's id
     * @return A cursor for the user's data
     */
    ArangoCursor<VPackSlice> getUserById(String userId) throws IOException;
    ArangoCursor<VPackSlice> getArticleById(String userId) throws IOException;

    /**
     * Get the trending articles for a specific user
     *
     * @param userId: the user seeking trending articles
     * @return list of trending articles
     */
    ArrayList<Article> getTrendingArticles(String userId) throws IOException;

}
