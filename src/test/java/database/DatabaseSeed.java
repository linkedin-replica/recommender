package database;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.linkedin.replica.recommender.database.DatabaseConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.linkedin.replica.recommender.utils.Configuration;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class DatabaseSeed {

    private static Configuration config;
    private static ArangoDatabase arangoDatabaseInstance;

    private DatabaseSeed() {
    }

    public static void init() throws IOException, ParseException, SQLException, ClassNotFoundException {
        Configuration.init("src/main/resources/config/app.config", "src/main/resources/config/arango.test.config",
                "src/main/resources/config/commands.config", "src/main/resources/config/redis.config",
                "src/main/resources/config/controller.config");
        config = Configuration.getInstance();
        String dbName = config.getArangoConfig("db.name");
        DatabaseConnection.init();
        ArangoDB arangoDB = DatabaseConnection.getInstance().getArangoDriver();
        try {
            arangoDB.createDatabase(dbName);
        }
        catch (ArangoDBException exception) {
            exception.printStackTrace();
        }

        arangoDatabaseInstance = arangoDB.db(dbName);
        insertUsers();
        insertJobs();
        insertArticles();
    }


    /**
     * Method to get json data from json file.
     *
     * @param filePath path to the json file containing the json data
     * @return Iterator of json data
     * @throws IOException
     */
    public static JSONArray getJSONData(String filePath) throws IOException, ParseException, ParseException {
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(new Scanner(new File(filePath)).useDelimiter("\\Z").next());
    }

    /**
     * feed the database with users specified in json file users.json
     *
     * @throws IOException
     * @throws ParseException
     */
    public static void insertUsers() throws IOException, ParseException {
        String collectionName = config.getArangoConfig("collection.users.name");
        try {
            arangoDatabaseInstance.createCollection(collectionName);
        } catch (ArangoDBException exception) {
           exception.printStackTrace();
        }

        BaseDocument userDocument;
        JSONArray users = getJSONData("src/main/resources/data/users.json");
        for (Object user : users) {
            JSONObject userObject = (JSONObject) user;
            userDocument = new BaseDocument();
            userDocument.addAttribute("userId", userObject.get("userId"));
            userDocument.addAttribute("firstName", userObject.get("firstName"));
            userDocument.addAttribute("lastName", userObject.get("lastName"));
            userDocument.addAttribute("headline", userObject.get("headline"));
            userDocument.addAttribute("industry", userObject.get("industry"));
            userDocument.addAttribute("skills", userObject.get("skills"));
            userDocument.addAttribute("friendsList", userObject.get("friendsList"));
            arangoDatabaseInstance.collection(collectionName).insertDocument(userDocument);
            System.out.println("New user document insert with key = " + userDocument.getId());
        }
    }

    /**
     * Insert articles specified in articles.json file to the database collection articles
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws ParseException
     */
    public static void insertArticles() throws IOException, ClassNotFoundException, SQLException, ParseException {
        String collectionName = config.getArangoConfig("collection.articles.name");

        try {
            arangoDatabaseInstance.createCollection(collectionName);

        } catch (ArangoDBException exception) {
            exception.printStackTrace();
        }
        BaseDocument articleDocument;
        JSONArray articles = getJSONData("src/main/resources/data/articles.json");
        for (Object article : articles) {
            JSONObject articleObject = (JSONObject) article;
            articleDocument = new BaseDocument();
            articleDocument.addAttribute("postId", articleObject.get("postId"));
            articleDocument.addAttribute("authorId", articleObject.get("authorId"));
            articleDocument.addAttribute("headline", articleObject.get("headline"));
            articleDocument.addAttribute("timestamp", articleObject.get("timestamp"));
            articleDocument.addAttribute("text", articleObject.get("text"));
            articleDocument.addAttribute("likesCount", articleObject.get("likesCount"));
            articleDocument.addAttribute("commentsCount", articleObject.get("commentsCount"));
            articleDocument.addAttribute("shares", articleObject.get("shares"));
            arangoDatabaseInstance.collection(collectionName).insertDocument(articleDocument);
            System.out.println("New article document insert with key = " + articleDocument.getId());
        }
    }

    /**
     * Insert jobs specified in jobs.json file to the database collection jobs
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws ParseException
     */
    public static void insertJobs() throws IOException, ClassNotFoundException, SQLException, ParseException {
        String collectionName = config.getArangoConfig("collection.jobs.name");
        try {
            arangoDatabaseInstance.createCollection(collectionName);

        } catch (ArangoDBException exception) {
            exception.printStackTrace();
        }
        BaseDocument jobDocument;
        JSONArray jobs = getJSONData("src/main/resources/data/jobs.json");
        for (Object job : jobs) {
            JSONObject jobObject = (JSONObject) job;
            jobDocument = new BaseDocument();
            jobDocument.addAttribute("jobId", jobObject.get("jobId"));
            jobDocument.addAttribute("positionName", jobObject.get("positionName"));
            jobDocument.addAttribute("companyName", jobObject.get("companyName"));
            jobDocument.addAttribute("companyId", jobObject.get("companyId"));
            jobDocument.addAttribute("requiredSkills", jobObject.get("requiredSkills"));
            arangoDatabaseInstance.collection(collectionName).insertDocument(jobDocument);
            System.out.println("New job document insert with key = " + jobDocument.getId());
        }
    }

    /**
     * Delete jobs collection from the database if it exists
     *
     * @throws ArangoDBException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public static void deleteAllJobs() throws ArangoDBException, IOException {
        String collectionName = config.getArangoConfig("collection.jobs.name");
        try {
            arangoDatabaseInstance.collection(collectionName).drop();
        } catch (ArangoDBException exception) {
            if (exception.getErrorNum() == 1228) {
                System.out.println("Database not found");
            }
        }
        System.out.println("Jobs collection is dropped");
    }

    /**
     * Drop specified database from Arango Driver
     *
     * @throws IOException
     */
    public static void dropDatabase() throws IOException {
        try {
            arangoDatabaseInstance.drop();
        } catch (ArangoDBException exception) {
            if (exception.getErrorNum() == 1228) {
                System.out.println("Database not found");
            } else
                throw exception;
        }
    }

    /**
     * Closing connection to the database.
     *
     * @throws ArangoDBException
     * @throws IOException
     */
    public static void closeDBConnection() throws ArangoDBException, IOException {
        DatabaseConnection.getInstance().getArangoDriver().shutdown();
    }


}
