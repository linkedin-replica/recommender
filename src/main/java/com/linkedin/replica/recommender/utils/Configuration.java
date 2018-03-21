package com.linkedin.replica.recommender.utils;

import com.linkedin.replica.recommender.cache.handlers.CacheHandler;
import com.linkedin.replica.recommender.database.handlers.DatabaseHandler;
import com.linkedin.replica.recommender.commands.Command;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Configuration {

    private Properties commandConfig = new Properties();
    private Properties appConfig = new Properties();
    private Properties arangoConfig = new Properties();
    private Properties redisConfig = new Properties();
    private Properties controllerConfig = new Properties();

    private static Configuration instance;
    private boolean isAppConfigModified;
    private boolean isArangoConfigModified;
    private boolean isCommandsConfigModified;

    private String appConfigPath;
    private String arangoConfigPath;
    private String commandsConfigPath;

    private Configuration(String appConfigPath, String arangoConfigPath, String commandsConfigPath,
                          String redisConfigPath, String controllerConfigPath) throws IOException {
        populateWithConfig(this.appConfigPath = appConfigPath, appConfig);
        populateWithConfig(this.arangoConfigPath = arangoConfigPath, arangoConfig);
        populateWithConfig(this.commandsConfigPath = commandsConfigPath, commandConfig);
        populateWithConfig(redisConfigPath, redisConfig);
        populateWithConfig(controllerConfigPath, controllerConfig);
    }

    private static void populateWithConfig(String configFilePath, Properties properties) throws IOException {
        FileInputStream inputStream = new FileInputStream(configFilePath);
        properties.load(inputStream);
        inputStream.close();
    }

    public static void init(String appConfigPath, String arangoConfigPath, String commandsConfigPath, String redisConfigPath, String controllerConfigPath) throws IOException {
        instance = new Configuration(appConfigPath, arangoConfigPath, commandsConfigPath, redisConfigPath, controllerConfigPath);
    }

    public static Configuration getInstance() {
        return instance;
    }

    public Class getCommandClass(String commandName) throws ClassNotFoundException {
        String commandsPackageName = Command.class.getPackage().getName() + ".impl";
        String commandClassPath = commandsPackageName + '.' + commandConfig.get(commandName);
        return Class.forName(commandClassPath);
    }

    public Class getDatabaseHandlerClass(String commandName) throws ClassNotFoundException {
        String handlerPackageName = DatabaseHandler.class.getPackage().getName() + ".impl";
        String handlerClassPath = handlerPackageName + "." + commandConfig.get(commandName + ".db.handler");
        return Class.forName(handlerClassPath);
    }

    public Class getCacheHandlerClass(String commandName) throws ClassNotFoundException {
        String handlerPackageName = CacheHandler.class.getPackage().getName() + ".impl";
        String handlerClassPath = handlerPackageName + "." + commandConfig.get(commandName + ".cache.handler");
        return Class.forName(handlerClassPath);
    }

    public String getAppConfig(String key) {
        return appConfig.getProperty(key);
    }

    public String getArangoConfig(String key) {
        return arangoConfig.getProperty(key);
    }

    public String getCommandConfig(String key) {
        return commandConfig.getProperty(key);
    }

    public String getControllerConfig(String key) {
        return controllerConfig.getProperty(key);
    }

    public String getRedisConfig(String key) {
        return redisConfig.getProperty(key);
    }

    public void setAppConfig(String key, String val) {
        if (val != null)
            appConfig.setProperty(key, val);
        else
            appConfig.remove(key); // remove property if val is null

        isAppConfigModified = true;
    }

    public void setArrangoConfig(String key, String val) {
        if (val != null)
            arangoConfig.setProperty(key, val);
        else
            arangoConfig.remove(key); // remove property if val is null

        isArangoConfigModified = true;
    }

    public void setCommandsConfig(String key, String val) {
        if (val != null)
            commandConfig.setProperty(key, val);
        else
            commandConfig.remove(key); // remove property if val is null

        isCommandsConfigModified = true;
    }

    /**
     * Commit changes to write modifications in configuration files
     *
     * @throws IOException
     */
    public void commit() throws IOException {
        if (isAppConfigModified) {
            writeConfig(appConfigPath, appConfig);
            isAppConfigModified = false;
        }

        if (isArangoConfigModified) {
            writeConfig(arangoConfigPath, arangoConfig);
            isArangoConfigModified = false;
        }

        if (isCommandsConfigModified) {
            writeConfig(commandsConfigPath, commandConfig);
            isCommandsConfigModified = false;
        }
    }

    private void writeConfig(String filePath, Properties properties) throws IOException {
        // delete configuration file and then re-write it
        Files.deleteIfExists(Paths.get(filePath));
        OutputStream out = new FileOutputStream(filePath);
        properties.store(out, "");
        out.close();
    }

    public String getCommandsConfig(String key) { return commandConfig.getProperty(key); }

}