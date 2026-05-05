package com.bigo.tindatrack.SQLite_Database.SalesManagement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class SalesTableManagement {

    public static void createSalesTable() {
        String query = "CREATE TABLE IF NOT EXISTS sales (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " owner_id INTEGER NOT NULL," +        // Links to the user who made the sale
                " product_id INTEGER," +               // Links to the original product
                " name TEXT NOT NULL," +
                " quantity INTEGER NOT NULL," +
                " sale_date TEXT NOT NULL," +
                " FOREIGN KEY (owner_id) REFERENCES users(id)," +
                " FOREIGN KEY (product_id) REFERENCES products(id)" +
                ");";

        try (Connection connected = connect(); Statement stmt = connected.createStatement()) {

            stmt.execute(query);
            System.out.println("SUCCESSFULLY CREATED TABLE: sales");

        } catch (SQLException e) {
            System.err.println("FAILED CREATING TABLE sales: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
