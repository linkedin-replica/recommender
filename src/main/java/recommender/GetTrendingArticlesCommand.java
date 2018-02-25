package recommender;

import models.Command;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Implementation of GetTrendingArticles command functionality.
 */

public class GetTrendingArticlesCommand extends Command {

    public GetTrendingArticlesCommand(HashMap<String, String> args) {
        super(args);
    }

    public LinkedHashMap execute() throws IOException {
        LinkedHashMap<String, Object> results = new LinkedHashMap<>();
        // call dbHandler to get trendingArticles and return results in the results map as key-value pair
        results.put("results", this.dbHandler.getTrendingArticles(this.args.get("userId")));
        return results;
    }
}
