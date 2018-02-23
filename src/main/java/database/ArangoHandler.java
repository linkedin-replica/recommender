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
import java.util.Iterator;
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

        ArangoCursor<VPackSlice> userCursor = ArangoHandler.getUserById(userId);
        VPackSlice userSkills = userCursor.next().get("skills");
        //query for getting jobListing if there is a match between this jobListing and requesting user's skills
        String query = "FOR job IN jobs FILTER "
                + "COUNT(INTERSECTION(@userSkills, job.requiredSkills)) != 0 "
                + "RETURN job";
        //bind variables
        Map<String, Object> bindVars = new MapBuilder().put("userSkills", userSkills).get();

        //execute query
        ArangoCursor<JobListing> cursor = dbInstance.query(query, bindVars,null, JobListing.class);
        final ArrayList<JobListing> returnedResults = new ArrayList<>();
        cursor.forEachRemaining(returnedResults::add);
        return returnedResults;
    }

    public static ArangoCursor<VPackSlice> getUserById(String userId) throws IOException {
        String dbName = config.getConfig("db.name");
        String userCollectionName = config.getConfig("collection.users.name");
        String query = "FOR u IN users FILTER "
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
    public ArrayList<Article> getTrendingArticles(int userId) {
        return null;
    }

    /**
     * Close a connection with the database
     */
    public void disconnect() {
        //TODO
    }
}
