package com.bigo.tindatrack.SQLite_Database.productsManagement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class productTableManagement {
    public static void createProductTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (\n" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT NOT NULL,\n" +
                " quantity INTEGER NOT NULL DEFAULT 0,\n" +
                " expiry_date TEXT,\n"+");";

        try(Connection connected = connect(); Statement stmt = connected.createStatement()){
            stmt.execute(query);
            System.out.println("SUCCESSFULLY CREATED TABLE");
        }catch(SQLException e){
            System.err.println("FAILED CREATING TABLE : " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createProductTable();
    }

}
