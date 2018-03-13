package database;

import com.arangodb.ArangoDB;
import com.linkedin.replica.recommender.database.DatabaseConnection;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.linkedin.replica.recommender.utils.Configuration;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class DatabaseSeedTest {
    private static Configuration config;


    @BeforeClass
    public static void Setup() throws IOException, ParseException, SQLException, ClassNotFoundException {
        DatabaseSeed.init();
        config = Configuration.getInstance();
    }

    @Test
    public void testJobsInsertion() throws IOException, ParseException, SQLException, ClassNotFoundException {
        String dbName = config.getArangoConfig("db.name");
        String collectionName = config.getArangoConfig("collection.jobs.name");
        ArangoDB arangoDB = DatabaseConnection.getInstance().getArangoDriver();
        int size = DatabaseSeed.getJSONData("src/main/resources/data/jobs.json").size();
        System.out.println(size);
        assertEquals("Database was not created", true, arangoDB.getDatabases().contains(dbName));
        assertEquals("Collection was not created", true, arangoDB.db(dbName).collection(collectionName).exists());
        System.out.println(arangoDB.db(dbName).collection(collectionName).count().getCount());
        assertEquals("Wrong number of documents", true, arangoDB.db(dbName).collection(collectionName).count().getCount() == size);
    }

    @Test
    public void testUsersInsertion() throws IOException, ParseException {
        String dbName = config.getArangoConfig("db.name");
        String collectionName = config.getArangoConfig("collection.users.name");
        ArangoDB arangoDB = DatabaseConnection.getInstance().getArangoDriver();
        int size = DatabaseSeed.getJSONData("src/main/resources/data/users.json").size();
        assertEquals("Database was not created", true, arangoDB.getDatabases().contains(dbName));
        assertEquals("Collection was not created", true, arangoDB.db(dbName).collection(collectionName).exists());
        assertEquals("Wrong number of documents", true, arangoDB.db(dbName).collection(collectionName).count().getCount() == size);
    }

    @Test
    public void testJobsDeletions() throws IOException, ParseException {
        String dbName = config.getArangoConfig("db.name");
        String collectionName = config.getArangoConfig("collection.jobs.name");
        ArangoDB arangoDB = DatabaseConnection.getInstance().getArangoDriver();
        int size = DatabaseSeed.getJSONData("src/main/resources/data/users.json").size();
        DatabaseSeed.deleteAllJobs();
        assertEquals("Jobs collection should be dropped", false, arangoDB.db(dbName).collection(collectionName).exists());
    }

    @Test
    public void testDropDatabase() throws IOException {
        String dbName = config.getArangoConfig("db.name");
        DatabaseSeed.dropDatabase();
        ArangoDB arangoDB = DatabaseConnection.getInstance().getArangoDriver();
        assertEquals("Database should be dropped", false, arangoDB.db(dbName).exists());
    }

    @AfterClass
    public static void teardown() throws IOException {
        DatabaseSeed.deleteAllJobs();
        //databaseSeed.deleteAllUsers();
        DatabaseSeed.dropDatabase();
    }


}
