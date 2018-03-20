package com.linkedin.replica.recommender.services;

import com.linkedin.replica.recommender.cache.handlers.CacheHandler;
import com.linkedin.replica.recommender.commands.Command;
import com.linkedin.replica.recommender.database.handlers.DatabaseHandler;
import com.linkedin.replica.recommender.utils.Configuration;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class RecommendationService {
    private Configuration config;

    public RecommendationService() throws IOException {
        config = Configuration.getInstance();
    }

    public Object serve(String commandName, HashMap<String, String> args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Class<?> commandClass = config.getCommandClass(commandName);
        Constructor constructor = commandClass.getConstructor(HashMap.class);
        Command command = (Command) constructor.newInstance(args);

        Class<?> databaseHandlerClass = config.getDatabaseHandlerClass(commandName);
        Class<?> CacheHandlerClass = config.getCacheHandlerClass(commandName);

        DatabaseHandler dbHandler = (DatabaseHandler) databaseHandlerClass.newInstance();
        CacheHandler cacheHandler = (CacheHandler) CacheHandlerClass.newInstance();

        command.setDbHandler(dbHandler);
        command.setCacheHandler(cacheHandler);

        return command.execute();
    }
}