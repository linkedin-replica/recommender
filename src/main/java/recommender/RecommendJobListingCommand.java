package recommender;

import models.Command;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *  Implementation of RecommendJobListing command functionality.
 */

public class RecommendJobListingCommand extends Command {

    public RecommendJobListingCommand(HashMap<String, String> args) {
        super(args);
    }

    @Override
    public LinkedHashMap<String, Object> execute() throws IOException {

        LinkedHashMap<String, Object> results = new LinkedHashMap<>();
        // call dbHandler to get recommendedJobs and return results in the results map as key-value pair
        results.put("results", this.dbHandler.getRecommendedJobListing(this.args.get("userId")));
        return results;
    }
}
