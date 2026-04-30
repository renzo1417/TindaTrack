package com.bigo.tindatrack.SQLite_Database.productsManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class ProductManagement {

    // for the backend logic PLEASE PLESAE utilize the boolean funcitonality of this method
    public static boolean addProduct(String name, int quantity, String expiry){
        String query = "INSERT INTO products(name, quantity, expiry_date) VALUES (?,?,?)";
        try(Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query)){
            pstmt.setString(1, name);
            pstmt.setInt(2,quantity);
            pstmt.setString(3,expiry);

            pstmt.executeUpdate();
            System.out.println("SUCCESSFULLY ADDED PRODUCT: " + name + " - " + quantity + " - " + expiry);
            return true;
        } catch (SQLException e) {
            System.err.println("ERROR ADDING PRODUCT");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeProduct(String productName){
        String query = "DELETE FROM products WHERE name = ?";

        try(Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query)){
            pstmt.setString(1, productName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("ERROR removing PRODUCT");
            e.printStackTrace();
            return false;
        }
    }


    // FOR TESTING PURPOSES ONLY STILL NEEDS MORE IMPROVEMENTS DO NOT USE FOR UI YET
    public static void viewInventory(){
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
                 "  END;";
        try (Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query);) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\t SARISARI STORE INVENTORY\t");
            while (rs.next()) {
                System.out.printf("%-5d | %-15s | %-8d | %-12s | %-12s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("expiry_date"),
                        rs.getString("status"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
