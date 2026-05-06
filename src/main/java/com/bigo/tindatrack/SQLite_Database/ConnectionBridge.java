package com.bigo.tindatrack.SQLite_Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBridge {

    private static final String URL = "JDBC:sqlite:USER_tindaTracker.db";

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

}


//package com.bigo.tindatrack.SQLite_Database;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.nio.file.Paths;
//
//public class ConnectionBridge {
//
//    // Saves the DB file next to your project's working directory — consistent path every run
//    private static final String URL = "jdbc:sqlite:" +
//            Paths.get(System.getProperty("user.home"), "tindaTracker.db").toString();
//
//    public static Connection connect() {
//        Connection connectRoute = null;
//        try {
//            connectRoute = DriverManager.getConnection(URL);
//            System.out.println("SUCCESSFUL SQLITE DATABASE CONNECTION");
//        } catch (SQLException e) {
//            System.err.println("FAILED CONNECTING TO SQLITE DATABASE " + e.getMessage());
//            e.printStackTrace();
//        }
//        return connectRoute;
//    }
//}
