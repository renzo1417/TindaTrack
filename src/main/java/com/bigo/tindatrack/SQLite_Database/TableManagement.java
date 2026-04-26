package com.bigo.tindatrack.SQLite_Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// This CreateTable is similar to sir Serato's CreateTable for mysql + XAMPP, but instead Joel used SQLITE.
// Joel Notes:
// It wouldn't make sense to have a store management software to run online where the users would most probably
// be more comfortable in using this software in an offline environment.

// the db file is located under the Target Files, Db file name: USER_tindatracker.db

public class TableManagement {

    private static final String URL = "JDBC:sqlite:USER_tindaTracker.db";

    public TableManagement() throws SQLException {
    }

    public static Connection connect(){
        Connection connectRoute = null;
        try{
            connectRoute = DriverManager.getConnection(URL);
            System.out.println("SUCCESSFUL SQLITE DATABASE CONNECTION");
        }catch(SQLException e){
            System.err.println("FAILED CONNECTING TO SQLITE DATABASE " + e.getMessage() + "\n");
            e.printStackTrace();
        }

        return connectRoute;
    }

    public static void createUserTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (\n" +
                        " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " username TEXT NOT NULL UNIQUE,\n" +
                        " password TEXT NOT NULL\n" + ");";

        try(Connection connected = connect(); Statement stmt = connected.createStatement()){
            stmt.execute(query);
            System.out.println("SUCCESSFULLY CREATED TABLE");
        }catch(SQLException e){
            System.err.println("FAILED CREATING TABLE : " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createUserTable();
    }



}
