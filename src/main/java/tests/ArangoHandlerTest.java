package tests;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPack;
import com.arangodb.velocypack.VPackSlice;
import database.ArangoHandler;
import database.DatabaseConnection;
import database.DatabaseHandler;
import database.DatabaseSeed;
import models.JobListing;
import models.User;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ConfigReader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ArangoHandlerTest {
    private static DatabaseSeed databaseSeed;
    private static ConfigReader config;
    @BeforeClass
    public static void setup() throws IOException, ParseException, SQLException, ClassNotFoundException {
        databaseSeed = new DatabaseSeed();
        config = new ConfigReader("arango_names");
        databaseSeed.insertJobs();
        databaseSeed.insertUsers();
    }

    @Test
    public void testRecommendUsers() {
        //TODO
    }

    @Test
    public void testRecommendJobListing() throws IOException {
        assertEquals("Recommended job listing should have at least one skill in common with user skills", true, jobsMatchingUser("0") == 4);
        assertEquals("Recommended job list for user with id 3 should be empty", true, jobsMatchingUser("3") == 0);
    }

    @Test
    public void testRecommendTrendingArticles() {
        //TODO
    }

    @AfterClass
    public static void teardown() throws IOException {
        String dbName = config.getConfig("db.name");
        databaseSeed.deleteAllJobs();
        databaseSeed.deleteAllUsers();
        databaseSeed.dropDatabase(dbName);
    }

    /**
     * Get the number of jobs matching some user (have skills in common)
     * @param userId id of the user to check matching jobs for
     * @return number of matching jobs
     * @throws IOException
     */
    public static int jobsMatchingUser(String userId) throws IOException {
        DatabaseHandler databaseHandler = new ArangoHandler();
        ArangoCursor<VPackSlice> cursor = ArangoHandler.getUserById(userId);
        VPackSlice userSkills = cursor.next().get("skills");
        ArrayList<JobListing> recommendedJobs = databaseHandler.getRecommendedJobListing(userId);
        int jobs = 0;
        for(JobListing jobListing : recommendedJobs) {
            LOOP: for(String skill : jobListing.getRequiredSkills()) {
                for(int i = 0; i < userSkills.size(); ++i) {
                    String userSkill = userSkills.get(i).getAsString();
                    if(userSkill.equals(skill)) {
                        jobs++;
                        break LOOP;
                    }
                }
            }
        }
        System.out.println(jobs);
        return jobs;
    }



}
