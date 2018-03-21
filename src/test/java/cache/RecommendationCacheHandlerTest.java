package cache;

import com.google.gson.Gson;
import com.linkedin.replica.recommender.cache.Cache;
import com.linkedin.replica.recommender.cache.handlers.impl.JedisCacheHandler;
import com.linkedin.replica.recommender.models.Article;
import com.linkedin.replica.recommender.models.JobListing;
import com.linkedin.replica.recommender.models.User;
import com.linkedin.replica.recommender.services.RecommendationService;
import com.linkedin.replica.recommender.utils.Configuration;
import database.DatabaseSeed;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;


public class RecommendationCacheHandlerTest {
    private static Gson gson;
    private static RecommendationService recommendationService;
    private static Configuration config;
    private static String userId = "0";
    private static HashMap<String, String> args;

    @BeforeClass
    public static void setup() throws ClassNotFoundException, SQLException, ParseException, IOException {
        DatabaseSeed.init();
        Cache.init();
        gson = new Gson();
        config = Configuration.getInstance();
        recommendationService = new RecommendationService();
        args = new HashMap<>();
        args.put("userId", userId);
        args.put("toBeCached", "true");
    }

    @Test
    public void testJobsRecommendationCache() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        Object results = recommendationService.serve("recommendations.jobs", args);
        ArrayList<JobListing> jobListings = (ArrayList<JobListing>) results;
        String key = config.getRedisConfig("cache.jobs.name") + ":" + userId;
        ArrayList<JobListing> cachedJobs = JedisCacheHandler.getTList(key, JobListing.class);
        assertEquals("Cached jobs should match returned jobs", true, cachedJobs.containsAll(jobListings));
    }

    @Test
    public void testArticlesRecommendationCache() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        Object results = recommendationService.serve("recommendations.trending.articles", args);
        ArrayList<Article> articles = (ArrayList<Article>) results;
        String key = config.getRedisConfig("cache.articles.name") + ":" + userId;
        ArrayList<Article> cachedArticles = JedisCacheHandler.getTList(key, Article.class);
        assertEquals("Cached articles should match returned articles", true, cachedArticles.containsAll(articles));
    }

    @Test
    public void testUsersRecommendationCache() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        Object results = recommendationService.serve("recommendations.users", args);
        ArrayList<Article> users = (ArrayList<Article>) results;
        String key = config.getRedisConfig("cache.users.name") + ":" + userId;
        ArrayList<User> cachedUsers = JedisCacheHandler.getTList(key, User.class);
        assertEquals("Cached articles should match returned articles", true, cachedUsers.containsAll(users));
    }

    @AfterClass
    public static void teardown() throws IOException {
        DatabaseSeed.closeDBConnection();
        DatabaseSeed.dropDatabase();
        Cache.getInstance().destroyRedisPool();
    }


}
