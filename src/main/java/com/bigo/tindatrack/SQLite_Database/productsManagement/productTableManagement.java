package com.bigo.tindatrack.SQLite_Database.productsManagement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class productTableManagement {
    public static void createProductTable() {
        String query = "CREATE TABLE IF NOT EXISTS products (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " owner_id INTEGER NOT NULL," +
                " name TEXT NOT NULL," +
                " quantity INTEGER NOT NULL DEFAULT 0," +
                " expiry_date TEXT," +
                " FOREIGN KEY (owner_id) REFERENCES users(id)" +
                ");";

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
