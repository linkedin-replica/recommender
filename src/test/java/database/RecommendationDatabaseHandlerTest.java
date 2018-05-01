package database;

import com.arangodb.ArangoCursor;
import com.arangodb.velocypack.VPackSlice;
import com.linkedin.replica.recommender.database.handlers.RecommendationDatabaseHandler;
import com.linkedin.replica.recommender.database.handlers.impl.ArangoDatabaseHandler;
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

public class RecommendationDatabaseHandlerTest {
    private static RecommendationDatabaseHandler databaseHandler;
    private static Configuration config;

    @BeforeClass
    public static void setup() throws IOException, ParseException, SQLException, ClassNotFoundException {
        DatabaseSeed.init();
        databaseHandler =  new ArangoDatabaseHandler();
        config = Configuration.getInstance();
    }

    @Test
    public void testGetFriendsOfFriends() throws IOException, ParseException {
        JSONArray users = DatabaseSeed.getJSONData("src/main/resources/data/users.json");
        JSONObject firstUser = (JSONObject) users.get(0);
        JSONArray firstUserFriends = (JSONArray) firstUser.get("friendsList");
        int friendsOfFriendsCount = 0;
        for (Object friendId : firstUserFriends) {
            JSONObject friend = (JSONObject) users.get(Integer.parseInt((String) friendId));
            JSONArray friendsOfFriends = (JSONArray) friend.get("friendsList");
            for (Object friendOfFriend : friendsOfFriends)
                if (!firstUserFriends.contains(friendOfFriend))
                    friendsOfFriendsCount++;
        }
        ArrayList<User> friendsOfFriends = databaseHandler.getFriendsOfFriends("0");
        assertEquals("Recommended list should be of size " + friendsOfFriendsCount, friendsOfFriendsCount, friendsOfFriends.size());
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
        int numTrendingArticles = Integer.parseInt(config.getArangoConfig("count.trendingArticles"));

        assertEquals("Trending articles should have at most " + numTrendingArticles + "values", true, trendingArticles.size() <= numTrendingArticles);

        for (Article article :
                trendingArticles) {
            assertEquals("Trending article should have an Id", true, article.getPostId() != null);
            assertEquals("Trending article should have an authorId", true, article.getAuthorId() != null);
            assertEquals("Trending article should have a title", true, article.getTitle() != null);
            assertEquals("Trending article should have an authorName", true, article.getAuthorName() != null);
            assertEquals("Trending article should have miniText", true, article.getMiniText() != null);
            assertEquals("Trending article should have profilePicture", true, article.getAuthorProfilePictureUrl() != null);
            assertEquals("Trending article should have headline", true, article.getHeadline() != null);

            ArangoCursor<VPackSlice> cursor = databaseHandler.getArticleById(article.getPostId());
            VPackSlice expectedArticle = cursor.next();
            int expectedPeopleTalking = expectedArticle.get("likers").size() * likesWeight
                    + expectedArticle.get("commentsCount").getAsInt() * commentsWeight;

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
