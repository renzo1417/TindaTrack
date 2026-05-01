package com.bigo.tindatrack.SQLite_Database.productsManagement;

import java.lang.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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
        String query = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("Sorted by? ID, STATUS, QUANTITY(SELECT ONLY THE FIRST LETTER)");
        String option = sc.nextLine();
        option = String.valueOf(option.charAt(0)).toLowerCase(); // to only always get the first letter
        String sortedBy = null;
        switch (option){
            case "i":
                sortedBy = "ID";
                query = "SELECT id, name, quantity, expiry_date, " +
                        "CASE " +
                        "WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
                        "WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
                        "WHEN quantity < 10 THEN 'Low Stock' " +
                        "ELSE 'Safe' " +
                        "END as status " +
                        "FROM products " +
                        "ORDER BY id DESC";
                break;
            case "s":
                sortedBy = "Status";
                query = "SELECT id, name, quantity, expiry_date, " +
                        "CASE " +
                        "WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
                        "WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
                        "WHEN quantity < 10 THEN 'Low Stock' " +
                        "ELSE 'Safe' " +
                        "END as status " +
                        "FROM products " +
                        "ORDER BY " +
                        "CASE status " +
                        "WHEN 'Expired' THEN 1 " +
                        "WHEN 'Near Expiry' THEN 2 " +
                        "WHEN 'Low Stock' THEN 3 " +
                        "WHEN 'Safe' THEN 4 " +
                        "END;";
                break;
            case "q":
                sortedBy = "Quantity";
                query = "SELECT id, name, quantity, expiry_date, " +
                        "CASE " +
                        "WHEN date(expiry_date) < date('now', 'localtime') THEN 'Expired' " +
                        "WHEN date(expiry_date) <= date('now', '+7 days', 'localtime') THEN 'Near Expiry' " +
                        "WHEN quantity < 10 THEN 'Low Stock' " +
                        "ELSE 'Safe' " +
                        "END as status " +
                        "FROM products " +
                        "ORDER BY quantity DESC";
                break;
        }
        try (Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query);) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\t SARISARI STORE INVENTORY\t");
            // why i did this? bcos arrangement
            System.out.println("Sorted by " + sortedBy);
            System.out.printf("%-5s | %-15s | %-8s | %-12s | %-12s\n","ID","Product Name","Quantity","Expiry","Status");
            while (rs.next()) {
                // what does the %-5d etc means? d = integer and the s are Strings
                // but what is the -5? it only justifies for better arrangement to create better spacings
                // so that when a user inputs "delatang gwapo!" it would consume the 15 spaces created by the -15
                // and will not push further
                // TLDR : Arrange ment purposes
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
