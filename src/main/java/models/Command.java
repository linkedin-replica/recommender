package models;

import database.DatabaseHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Command {
    protected HashMap<String, String> args;
    protected DatabaseHandler dbHandler;

    public Command(HashMap<String, String> args) {
        this.args = args;
    }

    /**
     * Execute the command
     *
     * @return The output (if any) of the command
     */
    public abstract LinkedHashMap<String, Object> execute();

    public void setArgs(HashMap<String, String> args) {
        this.args = args;
    }

    public void setDbHandler(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }
}