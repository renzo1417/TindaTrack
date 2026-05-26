package com.bigo.tindatrack.SQLite_Database.SalesManagement;

import com.bigo.tindatrack.Sales.Sales;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class SalesManagement {

    private static final DateTimeFormatter SQLiteStandardDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static ObservableList<Sales> getSalesHistory(int ownerId) {
        ObservableList<Sales> salesList = FXCollections.observableArrayList();
        String query = "SELECT * FROM sales WHERE owner_id = ? ORDER BY sale_date DESC";

        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ownerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    salesList.add(new Sales(
                            rs.getInt("id"),
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getString("sale_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching sales: " + e.getMessage());
        }
        return salesList;
    }

    public static boolean recordSale(int ownerId, int productId, String name, int quantitySold) {
        String ts = LocalDateTime.now().format(SQLiteStandardDateFormat);

        String deductStockQuery = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND owner_id = ? AND quantity >= ?";
        String insertSaleQuery = "INSERT INTO sales (owner_id, product_id, name, quantity, sale_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            try (PreparedStatement updatePs = conn.prepareStatement(deductStockQuery); PreparedStatement insertPs = conn.prepareStatement(insertSaleQuery)) {

                // stock dedcution for sales
                updatePs.setInt(1, quantitySold);
                updatePs.setInt(2, productId);
                updatePs.setInt(3, ownerId);
                updatePs.setInt(4, quantitySold); // Ensures to not sell more than current stocks

                int rowsAffected = updatePs.executeUpdate();

                if (rowsAffected == 0) {
                    // if stocks are low or non existent then  rollback changes then return false
                    conn.rollback();
                    System.err.println("Sale failed: Insufficient stock for " + name);
                    return false;
                }

                // record sales in sales table
                insertPs.setInt(1, ownerId);
                insertPs.setInt(2, productId);
                insertPs.setString(3, name);
                insertPs.setInt(4, quantitySold);
                insertPs.setString(5, ts);
                insertPs.executeUpdate();

                // if everything worked then commit sales to database
                conn.commit();
                System.out.println("SUCCESSFULLY SOLD " + quantitySold + " of " + name);
                return true;

            } catch (SQLException ex) {
                conn.rollback(); // this line will roll everything back if something were to go wrong
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Error during checkout: " + e.getMessage());
            return false;
        }
    }
}
