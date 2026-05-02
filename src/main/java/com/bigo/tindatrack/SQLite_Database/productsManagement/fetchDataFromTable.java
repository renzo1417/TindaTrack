package com.bigo.tindatrack.SQLite_Database.productsManagement;

import com.bigo.tindatrack.Product.Product;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class fetchDataFromTable {

    public static ObservableList<Product> getInventoryOrderedByStatus(){
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        String query = "SELECT id, name, quantity, expiry_date, " +
                "CASE " +
                "  WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
                "  WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
                "  WHEN quantity < 10 THEN 'Low Stock' " +
                "  ELSE 'Safe' " +
                "END as status " +
                "FROM products " +
                "ORDER BY " +
                "  CASE status " +
                "    WHEN 'Expired' THEN 1 " +
                "    WHEN 'Near Expiry' THEN 2 " +
                "    WHEN 'Low Stock' THEN 3 " +
                "    WHEN 'Safe' THEN 4 " +
                "  END, quantity ASC;";

        try (Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            LocalDate localDateParsed = LocalDate.parse(rs.getString("expiry_date"));
            //public Product(String productName, int quantity, LocalDate expiryDate, String category)
            while (rs.next()) {
                Product product = new Product(rs.getString("name"),rs.getInt("quantity"),localDateParsed,"general");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory list: " + e.getMessage());
        }

        return productsList;
    }

    public static ObservableList<Product> getInventoryOrderedByQuantity(){
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        String query = "SELECT id, name, quantity, expiry_date, " +
                "CASE " +
                "  WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
                "  WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
                "  WHEN quantity < 10 THEN 'Low Stock' " +
                "  ELSE 'Safe' " +
                "END as status " +
                "FROM products " +
                "ORDER BY quantity DESC;";

        try (Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            LocalDate localDateParsed = LocalDate.parse(rs.getString("expiry_date"));
            //public Product(String productName, int quantity, LocalDate expiryDate, String category)
            while (rs.next()) {
                Product product = new Product(rs.getString("name"),rs.getInt("quantity"),localDateParsed,"general");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory list: " + e.getMessage());
        }

        return productsList;
    }

    public static ObservableList<Product> getInventoryOrderedByID(){
        ObservableList<Product> productsList = FXCollections.observableArrayList();
        String query = "SELECT id, name, quantity, expiry_date, " +
                "CASE " +
                "  WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
                "  WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
                "  WHEN quantity < 10 THEN 'Low Stock' " +
                "  ELSE 'Safe' " +
                "END as status " +
                "FROM products " +
                "ORDER BY id ASC;";

        try (Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            LocalDate localDateParsed = LocalDate.parse(rs.getString("expiry_date"));
            //public Product(String productName, int quantity, LocalDate expiryDate, String category)
            while (rs.next()) {
                Product product = new Product(rs.getString("name"),rs.getInt("quantity"),localDateParsed,"general");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory list: " + e.getMessage());
        }

        return productsList;
    }

}
