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
    private ConfigReader config;
    private ArangoDatabase dbInstance;
    private ArangoCollection collection;
    private String collectionName;

    public ArangoHandler() throws IOException {
        // read arango constants
        config = new ConfigReader("arango_names");

        // init db
        ArangoDB arangoDriver = DatabaseConnection.getDBConnection().getArangoDriver();
        collectionName = config.getConfig("collection.recommendations.name");
        dbInstance = arangoDriver.db(config.getConfig("db.name"));
        collection = dbInstance.collection(collectionName);
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
    public ArrayList<JobListing> getRecommendedJobListing(String userId) {
        String jobCollectionName = config.getConfig("collection.job.name");
        String userCollectionName = config.getConfig("collection.user.name");
        BaseDocument document = dbInstance.collection(userCollectionName).getDocument(userId, BaseDocument.class);
        String [] userSkills = (String []) document.getAttribute("skill");

        //query for getting jobListing if there is a match between this jobListing and requesting user's skills
        String query = "FOR job IN %s FILTER "
                + "@skills ANY IN job.requiredSkills "
                + "RETURN job";
        //bind variables
        Map<String, Object> bindVars = new MapBuilder().put("skills", userSkills).get();

        //execute query
        ArangoCursor<JobListing> cursor = dbInstance.query(query, bindVars,null, JobListing.class);

        final ArrayList<JobListing> returnedResults = new ArrayList<>();
        cursor.forEachRemaining(returnedResults::add);
        return returnedResults;
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
