package cache;

import com.google.gson.Gson;
import com.linkedin.replica.recommender.cache.Cache;
import com.linkedin.replica.recommender.cache.handlers.CacheHandler;
import com.linkedin.replica.recommender.cache.handlers.RecommendationCacheHandler;
import com.linkedin.replica.recommender.cache.handlers.impl.JedisCacheHandler;
import com.linkedin.replica.recommender.models.JobListing;
import com.linkedin.replica.recommender.services.RecommendationService;
import com.linkedin.replica.recommender.utils.Configuration;
import database.DatabaseSeed;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class RecommendationCacheHandlerTest {
    private static Gson gson;
    private static RecommendationService recommendationService;
    private static Configuration config;
//    private String CACHE_FRIENDS = configuration.getRedisConfig("cache.friends.name");
//    private String CACHE_JOBS = configuration.getRedisConfig("cache.jobs.name");
//    private String CACHE_ARTICLES = configuration.getRedisConfig("cache.articles.name");
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
    public void testJobRecommendationCache() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        Object results = recommendationService.serve("recommendations.jobs", args);
        ArrayList<JobListing> jobListings = (ArrayList<JobListing>) results;
        Jedis cacheInstance = Cache.getInstance().getRedisPool().getResource();
        String key = config.getRedisConfig("cache.jobs.name") + ":" + userId;
        Set<String> cacheResults = cacheInstance.smembers(key);

        for (String result:
             cacheResults) {
            JobListing jobListing = gson.fromJson(result, JobListing.class);
            System.out.println(jobListing.getCompanyName());
        }
    }

}
