//package com.bigo.tindatrack.SQLite_Database.productsManagement;
//
//import com.bigo.tindatrack.Product.Product;
//import javafx.beans.Observable;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//
//import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;
//
//public class fetchDataFromTable {
//
//    public static ObservableList<Product> getInventoryOrderedByStatus(){
//        ObservableList<Product> productsList = FXCollections.observableArrayList();
//        String query = "SELECT id, name, quantity, expiry_date, " +
//                "CASE " +
//                "  WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
//                "  WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
//                "  WHEN quantity < 10 THEN 'Low Stock' " +
//                "  ELSE 'Safe' " +
//                "END as status " +
//                "FROM products " +
//                "ORDER BY " +
//                "  CASE status " +
//                "    WHEN 'Expired' THEN 1 " +
//                "    WHEN 'Near Expiry' THEN 2 " +
//                "    WHEN 'Low Stock' THEN 3 " +
//                "    WHEN 'Safe' THEN 4 " +
//                "  END, quantity ASC;";
//
//        try (Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query);
//             ResultSet rs = pstmt.executeQuery()) {
//            LocalDate localDateParsed = LocalDate.parse(rs.getString("expiry_date"));
//            //public Product(String productName, int quantity, LocalDate expiryDate, String category)
//            while (rs.next()) {
//                Product product = new Product(rs.getString("name"),rs.getInt("quantity"),localDateParsed,"general");
//            }
//        } catch (SQLException e) {
//            System.err.println("Error fetching inventory list: " + e.getMessage());
//        }
//
//        return productsList;
//    }
//
//    public static ObservableList<Product> getInventoryOrderedByQuantity(){
//        ObservableList<Product> productsList = FXCollections.observableArrayList();
//        String query = "SELECT id, name, quantity, expiry_date, " +
//                "CASE " +
//                "  WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
//                "  WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
//                "  WHEN quantity < 10 THEN 'Low Stock' " +
//                "  ELSE 'Safe' " +
//                "END as status " +
//                "FROM products " +
//                "ORDER BY quantity DESC;";
//
//        try (Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query);
//             ResultSet rs = pstmt.executeQuery()) {
//            LocalDate localDateParsed = LocalDate.parse(rs.getString("expiry_date"));
//            //public Product(String productName, int quantity, LocalDate expiryDate, String category)
//            while (rs.next()) {
//                Product product = new Product(rs.getString("name"),rs.getInt("quantity"),localDateParsed,"general");
//            }
//        } catch (SQLException e) {
//            System.err.println("Error fetching inventory list: " + e.getMessage());
//        }
//
//        return productsList;
//    }
//
//    public static ObservableList<Product> getInventoryOrderedByID(){
//        ObservableList<Product> productsList = FXCollections.observableArrayList();
//        String query = "SELECT id, name, quantity, expiry_date, " +
//                "CASE " +
//                "  WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
//                "  WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
//                "  WHEN quantity < 10 THEN 'Low Stock' " +
//                "  ELSE 'Safe' " +
//                "END as status " +
//                "FROM products " +
//                "ORDER BY id ASC;";
//
//        try (Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query);
//             ResultSet rs = pstmt.executeQuery()) {
//            LocalDate localDateParsed = LocalDate.parse(rs.getString("expiry_date"));
//            //public Product(String productName, int quantity, LocalDate expiryDate, String category)
//            while (rs.next()) {
//                Product product = new Product(rs.getString("name"),rs.getInt("quantity"),localDateParsed,"general");
//            }
//        } catch (SQLException e) {
//            System.err.println("Error fetching inventory list: " + e.getMessage());
//        }
//
//        return productsList;
//    }
//
//
//
//}

package com.bigo.tindatrack.SQLite_Database.productsManagement;

import com.bigo.tindatrack.Product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class fetchDataFromTable {

    // Shared helper to map a ResultSet row → Product
    private static Product mapRow(ResultSet rs) throws SQLException {
        int       id         = rs.getInt("id");
        String    name       = rs.getString("name");
        int       quantity   = rs.getInt("quantity");
        String    status     = rs.getString("status");
        String    expiryStr  = rs.getString("expiry_date");
        LocalDate expiryDate = null;
        if (expiryStr != null && !expiryStr.trim().isEmpty()) {
            // This will change all date formats to a unified YYYY-MM-DD(SQLite Requirement)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
            expiryDate = LocalDate.parse(expiryStr, formatter);
        }
        Product p = new Product(name, quantity, expiryDate, "general");
        p.setId(id);         // make sure Product has setId()
        return p;
    }

    private static final String BASE_QUERY =
            "SELECT id, name, quantity, expiry_date, " +
                    "CASE " +
                    "  WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
                    "  WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
                    "  WHEN quantity < 10 THEN 'Low Stock' " +
                    "  ELSE 'Safe' " +
                    "END as status FROM products WHERE owner_id = ?";

    public static ObservableList<Product> getInventoryOrderedByStatus(int ownerId) {
        String query = BASE_QUERY +
                "ORDER BY CASE " +
                "  WHEN date(expiry_date) < date('now','localtime') THEN 1 " +
                "  WHEN date(expiry_date) <= date('now','+7 days','localtime') THEN 2 " +
                "  WHEN quantity < 10 THEN 3 ELSE 4 END, quantity ASC";
        return runQuery(query, ownerId);
    }

    public static ObservableList<Product> getInventoryOrderedByQuantity(int ownerId) {
        return runQuery(BASE_QUERY + "ORDER BY quantity DESC",ownerId);
    }

    public static ObservableList<Product> getInventoryOrderedByID(int ownerId) {
        return runQuery(BASE_QUERY + "ORDER BY id ASC",ownerId);
    }

    // ── NEW: used by NotificationService ─────────────────────────────────
    public static ObservableList<Product> getAllProducts(int ownerId) {
        return runQuery(BASE_QUERY + "ORDER BY id ASC",ownerId);
    }

    private static ObservableList<Product> runQuery(String query, int ownerId) {
        ObservableList<Product> list = FXCollections.observableArrayList();
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ownerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching inventory: " + e.getMessage());
        }
        return list;
    }
}
