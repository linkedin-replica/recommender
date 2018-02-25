package database;

import com.arangodb.ArangoDB;
import database.DatabaseConnection;
import database.DatabaseSeed;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ConfigReader;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class DatabaseSeedTest {
    private static DatabaseSeed databaseSeed;
    private static ConfigReader config;


    @BeforeClass
    public static void Setup() throws IOException {
        databaseSeed = new DatabaseSeed();
        config = ConfigReader.getInstance();
    }

    @Test
    public void testJobsInsertion() throws IOException, ParseException, SQLException, ClassNotFoundException {
        databaseSeed.insertJobs();
        String dbName = config.getArangoConfig("db.name");
        String collectionName = config.getArangoConfig("collection.jobs.name");
        ArangoDB arangoDB = DatabaseConnection.getDBConnection().getArangoDriver();
        int size = databaseSeed.getJSONData("src/main/resources/data/jobs.json").size();
        System.out.println(size);
        assertEquals("Database was not created", true, arangoDB.getDatabases().contains(dbName));
        assertEquals("Collection was not created", true, arangoDB.db(dbName).collection(collectionName).exists());
        System.out.println(arangoDB.db(dbName).collection(collectionName).count().getCount());
        assertEquals("Wrong number of documents", true, arangoDB.db(dbName).collection(collectionName).count().getCount() == size);
    }

    @Test
    public void testUsersInsertion() throws IOException, ParseException {
        databaseSeed.insertUsers();
        String dbName = config.getArangoConfig("db.name");
        String collectionName = config.getArangoConfig("collection.users.name");
        ArangoDB arangoDB = DatabaseConnection.getDBConnection().getArangoDriver();
        int size = databaseSeed.getJSONData("src/main/resources/data/users.json").size();
        assertEquals("Database was not created", true, arangoDB.getDatabases().contains(dbName));
        assertEquals("Collection was not created", true, arangoDB.db(dbName).collection(collectionName).exists());
        assertEquals("Wrong number of documents", true, arangoDB.db(dbName).collection(collectionName).count().getCount() == size);
    }

    @Test
    public void testJobsDeletions() throws IOException, ParseException {
        databaseSeed.insertUsers();
        String dbName = config.getArangoConfig("db.name");
        String collectionName = config.getArangoConfig("collection.jobs.name");
        ArangoDB arangoDB = DatabaseConnection.getDBConnection().getArangoDriver();
        int size = databaseSeed.getJSONData("src/main/resources/data/users.json").size();
        databaseSeed.deleteAllJobs();
        assertEquals("Jobs collection should be dropped", false, arangoDB.db(dbName).collection(collectionName).exists());
    }

    @Test
    public void testDropDatabase() throws IOException {
        String dbName = config.getArangoConfig("db.name");
        databaseSeed.dropDatabase(dbName);
        ArangoDB arangoDB = DatabaseConnection.getDBConnection().getArangoDriver();
        assertEquals("Database should be dropped", false, arangoDB.db(dbName).exists());
    }

    @AfterClass
    public static void teardown() throws IOException {
        String dbName = config.getArangoConfig("db.name");
        databaseSeed.deleteAllJobs();
        //databaseSeed.deleteAllUsers();
        databaseSeed.dropDatabase(dbName);
    }


}
