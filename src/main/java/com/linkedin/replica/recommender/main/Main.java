package com.linkedin.replica.recommender.main;

import com.linkedin.replica.recommender.utils.Configuration;
import com.linkedin.replica.recommender.database.DatabaseConnection;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public class Main {


    public void start(String... args) throws ClassNotFoundException, IOException, SQLException {
        if(args.length != 3)
            throw new IllegalArgumentException("Expected three arguments. 1- Database config file path "
                    + "2- Command config file path  3- Arango name file path 4- Redis name file path");

        // create singleton instance of Configuration class that will hold configuration files paths
        Configuration.init(args[0], args[1], args[2], args[3]);

        // create singleton instance of DatabaseConnection class that is responsible for making connections with databases
        DatabaseConnection.init();
    }

    public void shutdown() throws ClassNotFoundException, IOException, SQLException{
        DatabaseConnection.getInstance().closeConnections();
    }

    public static void main(String[] args) throws IOException, ParseException, SQLException, ClassNotFoundException {
        new Main().start(args);
    }
}

