package database;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import models.Article;
import models.JobListing;
import models.User;
import utils.ConfigReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ArangoHandler implements DatabaseHandler {
    private static ConfigReader config;
    private ArangoDatabase dbInstance;

    public ArangoHandler() throws IOException {
        // read arango constants
        config = new ConfigReader("arango_names");

        // init db
        ArangoDB arangoDriver = DatabaseConnection.getDBConnection().getArangoDriver();
        dbInstance = arangoDriver.db(config.getConfig("db.name"));
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
    public ArrayList<JobListing> getRecommendedJobListing(String userId) throws IOException {

        ArangoCursor<VPackSlice> userCursor = getUserById(userId);
        VPackSlice userSkills = userCursor.next().get("skills");
        //query for getting jobListing if there is a match between this jobListing and requesting user's skills
        String jobsCollectionName = config.getConfig("collection.jobs.name");
        String query = "FOR job IN @jobs FILTER "
                + "COUNT(INTERSECTION(@userSkills, job.requiredSkills)) != 0 "
                + "RETURN job";
        //bind variables
        Map<String, Object> bindVars = new MapBuilder().put("jobs", jobsCollectionName).put("userSkills", userSkills).get();

        //execute query
        ArangoCursor<JobListing> cursor = dbInstance.query(query, bindVars, null, JobListing.class);
        final ArrayList<JobListing> returnedResults = new ArrayList<>();
        cursor.forEachRemaining(returnedResults::add);
        return returnedResults;
    }

    public ArangoCursor<VPackSlice> getUserById(String userId) throws IOException {
        String dbName = config.getConfig("db.name");
        String userCollectionName = config.getConfig("collection.users.name");
        String query = "FOR u IN @userCollectionName FILTER "
                + "u.userId == @userId "
                + "RETURN u";
        Map<String, Object> bindVars = new MapBuilder().put("userId", userId).get();
        ArangoDatabase db = DatabaseConnection.getDBConnection().getArangoDriver().db(dbName);
        return db.query(query, bindVars, null, VPackSlice.class);
    }

    /**
     * Get the trending articles for a specific user
     *
     * @param userId : the user seeking trending articles
     * @return list of trending articles
     */
    public ArrayList<Article> getTrendingArticles(String userId) throws IOException {

        String articlesCollectionName = config.getConfig("collection.articles.name");
        int likesWeight = Integer.parseInt(config.getConfig("weights.like"));
        int commentsWeight = Integer.parseInt(config.getConfig("weights.comments"));
        int sharesWeight = Integer.parseInt(config.getConfig("weights.shares"));
        int numTrendingArticles = Integer.parseInt(config.getConfig("count.trendingArticles"));

        //query for getting the top n articles sorted by likes, comments and shares with their weights from the most recent 100 articles
        String query = "FOR article IN @articles"
                + "SORT article.timestamp DESC"
                + "LIMIT 0, 100"
                + "LET comments = articles.commentsCount * @commentsWeight"
                + "LET likes = articles.likesCount * @likesWeight"
                + "LET shares = LENGTH(article.shares) * @sharesWeight"
                + "SORT shares, comments, likes DESC"
                + "LIMIT 0, @numArticles"
                + "RETURN article";

        //bind variables
        Map<String, Object> bindVars = new MapBuilder()
                .put("articles", articlesCollectionName)
                .put("likesWeight", likesWeight)
                .put("commentsWeight", commentsWeight)
                .put("sharesWeight", sharesWeight)
                .put("numArticles", numTrendingArticles)
                .get();

        //execute query
        ArangoCursor<Article> cursor = dbInstance.query(query, bindVars, null, Article.class);
        final ArrayList<Article> returnedResults = new ArrayList<>();
        cursor.forEachRemaining(returnedResults::add);
        return returnedResults;
    }

    /**
     * Close a connection with the database
     */
    public void disconnect() {
        //TODO
    }
}
