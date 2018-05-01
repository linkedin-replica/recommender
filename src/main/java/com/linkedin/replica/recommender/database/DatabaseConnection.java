package com.linkedin.replica.recommender.database;

import com.arangodb.ArangoDB;
import com.linkedin.replica.recommender.utils.Configuration;

import javax.xml.crypto.Data;
import java.io.IOException;

/**
 * A singleton class carrying a database instance
 */
public class DatabaseConnection {
    private ArangoDB arangoDriver;
    private Configuration config;

    private static DatabaseConnection dbConnection;

    private DatabaseConnection() throws IOException {
        config = Configuration.getInstance();
        initializeArangoDB();
    }

    private void initializeArangoDB() {
        arangoDriver = new ArangoDB.Builder()
                .user(config.getArangoConfig("arangodb.user"))
                .password(config.getArangoConfig("arangodb.password"))
                .build();
    }

    /**
     * Get a singleton DB instance
     *
     * @return The DB instance
     */
    public static DatabaseConnection getInstance() throws IOException {
        if(dbConnection == null) {
            synchronized (DatabaseConnection.class) {
                if(dbConnection == null)
                    dbConnection = new DatabaseConnection();
            }
        }
        return dbConnection;
    }


    public ArangoDB getArangoDriver() {
        return arangoDriver;
    }

    public void closeConnections() {
        arangoDriver.shutdown();
    }
}
