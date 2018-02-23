package database;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.ConfigReader;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class DatabaseSeed {

    private ArangoDB arangoDriver;
    private DatabaseHandler dbHandler;
    private static ConfigReader config;

    public DatabaseSeed() throws FileNotFoundException, IOException{
        config = new ConfigReader("arango_names");
    }


    /**
     * Method to get json data from json file.
     * @param filePath path to the json file containing the json data
     * @return Iterator of json data
     * @throws IOException
     */
    public static JSONArray getJSONData(String filePath) throws IOException, ParseException, ParseException {
        JSONParser parser = new JSONParser();
        JSONArray content = (JSONArray) parser.parse(new Scanner(new File(filePath)).useDelimiter("\\Z").next());
        return content;
    }

    /**
     * Insert jobs specified in jobs.json file to the database collection jobs
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws ParseException
     */
    public static void insertJobs() throws IOException, ClassNotFoundException, SQLException, ParseException {

        ArangoDB arangoDB = DatabaseConnection.getDBConnection().getArangoDriver();
        String dbName = config.getConfig("db.name");
        String collectionName = config.getConfig("collection.jobs.name");

        try{
            arangoDB.db(dbName).createCollection(collectionName);

        }catch(ArangoDBException exception){
            // check if exception was raised because that database was not created
            if(exception.getErrorNum() == 1228){
                arangoDB.createDatabase(dbName);
                arangoDB.db(dbName).createCollection(collectionName);
            } else if(exception.getErrorNum() == 1207) {
                // NoOP
            }else {
                throw exception;
            }
        }
        int counter = 0;
        BaseDocument newDoc;
        JSONArray jsonData = getJSONData("src/main/resources/data/jobs.json");
        for (int i = 0; i < jsonData.size(); ++i){
            JSONObject curJSONObject = (JSONObject) jsonData.get(i);
            newDoc = new BaseDocument();
            newDoc.addAttribute("JobID", counter++);
            newDoc.addAttribute("positionName", curJSONObject.get("positionName"));
            newDoc.addAttribute("companyName", curJSONObject.get("companyName"));
            newDoc.addAttribute("companyId", curJSONObject.get("companyId"));
            newDoc.addAttribute("requiredSkills", curJSONObject.get("requiredSkills"));
            arangoDB.db(dbName).collection(collectionName).insertDocument(newDoc);
            System.out.println("New job document insert with key = " + newDoc.getId());
        }
    }

    /**
     * Delete jobs collection from the database if it exists
     * @throws ArangoDBException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public static void deleteAllJobs() throws ArangoDBException, FileNotFoundException, ClassNotFoundException, IOException, SQLException{
       		String dbName = config.getConfig("db.name");
       		String collectionName = config.getConfig("collection.jobs.name");
       		try {
                DatabaseConnection.getDBConnection().getArangoDriver().db(dbName).collection(collectionName).drop();
            } catch(ArangoDBException exception) {
       		    if(exception.getErrorNum() == 1228) {
                    System.out.println("Database not found");
                }
            }
       		System.out.println("Jobs collection is dropped");
    }

    /**
     * Drop specified database from Arango Driver
     * @param dbName Database name to be dropped
     * @throws IOException
     */
    public static void dropDatabase(String dbName) throws IOException {
        try {
            DatabaseConnection.getDBConnection().getArangoDriver().db(dbName).drop();
        } catch(ArangoDBException exception) {
            if(exception.getErrorNum() == 1228) {
                System.out.println("Database not found");
            } else
                throw exception;
        }
    }

    /**
     * Closing connection to the database.
     * @throws ArangoDBException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public static void closeDBConnection() throws ArangoDBException, FileNotFoundException, ClassNotFoundException, IOException, SQLException {

        DatabaseConnection.getDBConnection().getArangoDriver().shutdown();
    }
}
