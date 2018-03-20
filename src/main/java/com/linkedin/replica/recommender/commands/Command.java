package com.linkedin.replica.recommender.commands;


import com.linkedin.replica.recommender.cache.handlers.CacheHandler;
import com.linkedin.replica.recommender.database.handlers.DatabaseHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Command {
    protected HashMap<String, String> args;
    protected DatabaseHandler dbHandler;
    protected CacheHandler cacheHandler;

    public Command(HashMap<String, String> args) {
        this.args = args;
    }

    /**
     * Execute the command
     *
     * @return The output (if any) of the command
     */
    public abstract Object execute() throws IOException;

    /**
     * Set the configured db handler
     *
     * @param dbHandler: The configured db handler
     */
    public void setDbHandler(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    /**
     * Set the configured cache handler
     *
     * @param cacheHandler: The configured db handler
     */
    public void setCacheHandler(CacheHandler cacheHandler) { this.cacheHandler = cacheHandler; }

    protected void validateArgs(String[] requiredArgs) {
        for (String arg : requiredArgs)
            if (!args.containsKey(arg)) {
                String exceptionMsg = String.format("Cannot execute command. %s argument is missing", arg);
                throw new IllegalArgumentException(exceptionMsg);
            }
    }
}