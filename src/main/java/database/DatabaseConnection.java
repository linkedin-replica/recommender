package database;

import com.arangodb.ArangoDB;
import utils.ConfigReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A singleton class carrying a database instance
 */
public class DatabaseConnection {
    private ArangoDB arangoDriver;
    private DatabaseHandler dbHandler;
    private ConfigReader config;

    private static DatabaseConnection dbConnection;

    private DatabaseConnection() throws IOException {
        config = ConfigReader.getInstance();

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
    public static DatabaseConnection getDBConnection() throws IOException {
        if(dbConnection == null) {
            synchronized (DatabaseConnection.class) {
                if (dbConnection == null)
                    dbConnection = new DatabaseConnection();
            }
        }
        return dbConnection;
    }


    public ArangoDB getArangoDriver() {
        return arangoDriver;
    }
}
