package com.bigo.tindatrack.SQLite_Database.StockManagement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class StockTableManagement {
    public static void createStockTable() {
        String query = "CREATE TABLE IF NOT EXISTS stockactivity (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "owner_id INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "oldQty INTEGER NOT NULL," +
                "newQty INTEGER NOT NULL," +
                "reason TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "FOREIGN KEY (owner_id) REFERENCES users(id)" +
                ");";

        try(Connection connected = connect(); Statement stmt = connected.createStatement()) {
            stmt.execute(query);
            System.out.println("SUCCESSFULLY CREATED TABLE : Stock activity");
        } catch (SQLException e) {
            System.err.println("FAILED CREATING TABLE : " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createStockTable();
    }
}
