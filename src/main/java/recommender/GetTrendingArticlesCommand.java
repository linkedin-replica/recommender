package recommender;

import models.Command;

import java.util.HashMap;

public class GetTrendingArticlesCommand extends Command{

    public GetTrendingArticlesCommand(HashMap<String, String> args) {
        super(args);
    }

    public String execute() {
        return null;
    }
}
