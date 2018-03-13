package database;

import com.arangodb.ArangoCursor;
import com.arangodb.velocypack.VPackSlice;
import com.linkedin.replica.recommender.database.handlers.RecommendationHandler;
import com.linkedin.replica.recommender.database.handlers.impl.ArangoHandler;
import com.linkedin.replica.recommender.models.Article;
import com.linkedin.replica.recommender.models.JobListing;
import com.linkedin.replica.recommender.models.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.linkedin.replica.recommender.utils.Configuration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RecommendationHandlerTest {
    private static RecommendationHandler databaseHandler;
    private static Configuration config;

    @BeforeClass
    public static void setup() throws IOException, ParseException, SQLException, ClassNotFoundException {
        DatabaseSeed.init();
        databaseHandler =  new ArangoHandler();
        config = Configuration.getInstance();
    }

    @Test
    public void testGetFriendsOfUser() throws IOException, ParseException {
        JSONArray users = DatabaseSeed.getJSONData("src/main/resources/data/users.json");
        JSONObject firstUser = (JSONObject) users.get(0);
        JSONArray firstUserFriends = (JSONArray) firstUser.get("friendsList");
        int friendsSize = firstUserFriends.size();
        JSONObject firstFriend = (JSONObject) firstUserFriends.get(0);
        String firstFriendName = (String) firstFriend.get("firstName");
        ArrayList<User> friends = databaseHandler.getFriendsOfUser("0");
        assertEquals("Friend list should be of size " + friendsSize, friendsSize, friends.size());
        assertEquals("First friend's first name should be " + firstFriendName, firstFriendName, friends.get(0).getFirstName());
    }

    @Test
    public void testRecommendJobListing() throws IOException {
        assertEquals("Recommended job listing should have at least one skill in common with user skills", true, jobsMatchingUser("0") == 4);
        assertEquals("Recommended job list for user with id 3 should be empty", true, jobsMatchingUser("3") == 0);
    }

    @Test
    public void testRecommendTrendingArticles() throws IOException, ParseException {
        ArrayList<Article> trendingArticles = databaseHandler.getTrendingArticles("0");
        int likesWeight = Integer.parseInt(config.getArangoConfig("weights.like"));
        int commentsWeight = Integer.parseInt(config.getArangoConfig("weights.comment"));
        int sharesWeight = Integer.parseInt(config.getArangoConfig("weights.share"));
        int numTrendingArticles = Integer.parseInt(config.getArangoConfig("count.trendingArticles"));

        assertEquals("Trending articles should have at most " + numTrendingArticles + "values", true, trendingArticles.size() <= numTrendingArticles);

        for (Article article :
                trendingArticles) {
            assertEquals("Trending article should have an Id", true, article.getPostId() != null);
            assertEquals("Trending article should have an authorId", true, article.getAuthorId() != null);
            assertEquals("Trending article should have a title", true, article.getTitle() != null);
            assertEquals("Trending article should have an authorFirstName", true, article.getAuthorFirstName() != null);
            assertEquals("Trending article should have an authorLastName", true, article.getAuthorLastName() != null);
            assertEquals("Trending article should have miniText", true, article.getMiniText() != null);

            ArangoCursor<VPackSlice> cursor = databaseHandler.getArticleById(article.getPostId());
            VPackSlice expectedArticle = cursor.next();

            int expectedPeopleTalking = Integer.parseInt(expectedArticle.get("likesCount").toString()) * likesWeight
                    + Integer.parseInt(expectedArticle.get("commentsCount").toString()) * commentsWeight
                    + expectedArticle.get("shares").size() * sharesWeight;

            assertEquals("Trending article should have peopleTalking count", expectedPeopleTalking, article.getPeopleTalking());
        }
    }

    @AfterClass
    public static void teardown() throws IOException {
        DatabaseSeed.closeDBConnection();
        DatabaseSeed.dropDatabase();
    }

    /**
     * Get the number of jobs matching some user (have skills in common)
     * @param userId id of the user to check matching jobs for
     * @return number of matching jobs
     * @throws IOException
     */
    public int jobsMatchingUser(String userId) throws IOException {
        ArangoCursor<VPackSlice> cursor = databaseHandler.getUserById(userId);
        VPackSlice userSkills = cursor.next().get("skills");
        ArrayList<JobListing> recommendedJobs = databaseHandler.getRecommendedJobListing(userId);
        int jobs = 0;
        for (JobListing jobListing : recommendedJobs) {
            LOOP:
            for (String skill : jobListing.getRequiredSkills()) {
                for (int i = 0; i < userSkills.size(); ++i) {
                    String userSkill = userSkills.get(i).getAsString();
                    if (userSkill.equals(skill)) {
                        jobs++;
                        break LOOP;
                    }
                }
            }
        }
        return jobs;
    }


}
