package database;


import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
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
        config = ConfigReader.getInstance();

        // init db
        ArangoDB arangoDriver = DatabaseConnection.getDBConnection().getArangoDriver();
        dbInstance = arangoDriver.db(config.getArangoConfig("db.name"));
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
        String jobsCollectionName = config.getArangoConfig("collection.jobs.name");
        String query = "FOR job IN jobs FILTER "
                + "COUNT(INTERSECTION(@userSkills, job.requiredSkills)) != 0 "
                + "RETURN job";
        //bind variables
        Map<String, Object> bindVars = new MapBuilder().put("userSkills", userSkills).get();

        //execute query
        ArangoCursor<JobListing> cursor = dbInstance.query(query, bindVars, null, JobListing.class);
        final ArrayList<JobListing> returnedResults = new ArrayList<>();
        cursor.forEachRemaining(returnedResults::add);
        return returnedResults;
    }

    public ArangoCursor<VPackSlice> getUserById(String userId) throws IOException {
        String dbName = config.getArangoConfig("db.name");
        String userCollectionName = config.getArangoConfig("collection.users.name");
        String query = "FOR u IN users FILTER "
                + "u.userId == @userId "
                + "RETURN u";
        Map<String, Object> bindVars = new MapBuilder().put("userId", userId).get();
        ArangoDatabase db = DatabaseConnection.getDBConnection().getArangoDriver().db(dbName);
        return db.query(query, bindVars, null, VPackSlice.class);
    }

    public ArangoCursor<VPackSlice> getArticleById(String postId) throws IOException {
        String dbName = config.getArangoConfig("db.name");
        String articlesCollectionName = config.getArangoConfig("collection.articles.name");
        String query = "FOR article IN articles FILTER "
                + "article.postId == @postId "
                + "RETURN article";
        Map<String, Object> bindVars = new MapBuilder().put("postId", postId).get();
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

        String articlesCollectionName = config.getArangoConfig("collection.articles.name");
        int likesWeight = Integer.parseInt(config.getArangoConfig("weights.like"));
        int commentsWeight = Integer.parseInt(config.getArangoConfig("weights.comment"));
        int sharesWeight = Integer.parseInt(config.getArangoConfig("weights.share"));
        int numTrendingArticles = Integer.parseInt(config.getArangoConfig("count.trendingArticles"));

        //query for getting the top n articles sorted by likes, comments and shares with their weights from the most recent 50 articles
        String queryHeader = "FOR article IN articles ";

        String getAuthorQuery = "LET author = FIRST(FOR u IN users " +
                "FILTER u.userId == article.authorId " +
                "RETURN u) ";

        String sortArticlesByTimeQuery = "SORT article.timestamp DESC "
                + "LIMIT 0, 50 ";

        String sortArticlesByPopularityQuery = "LET comments = article.commentsCount * @commentsWeight "
                + "LET likes = article.likesCount * @likesWeight "
                + "LET shares = LENGTH(article.shares) * @sharesWeight "
                + "LET total = likes + shares + comments "
                + "SORT total DESC "
                + "LIMIT 0, @numArticles ";


        String returnQuery = "RETURN {" +
                "\"postId\": article.postId," +
                "\"authorId\": article.authorId," +
                "\"title\": article.headline," +
                "\"authorFirstName\": author.firstName," +
                "\"authorLastName\": author.lastName," +
                "\"miniText\": LEFT(article.text, 200)," +
                "\"peopleTalking\": total" +
                "}";

        String query = queryHeader + getAuthorQuery + sortArticlesByTimeQuery + sortArticlesByPopularityQuery
                + returnQuery;

        //bind variables
        Map<String, Object> bindVars = new MapBuilder()
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
