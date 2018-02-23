package recommender;

import models.Command;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class GetTrendingArticlesCommand extends Command{

    public GetTrendingArticlesCommand(HashMap<String, String> args) {
        super(args);
    }

    public LinkedHashMap execute() {
        return null;
    }
}
