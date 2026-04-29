package com.bigo.tindatrack.SQLite_Database.userManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;


public class CreateUser {

    // 1username, 2fullname, 3password, 4email, 5phoneNumber, 6storeName

    public static boolean createUser(String username, String fullname, String password, String email, String phoneNumber, String storeName){
        String query = "INSERT INTO users(username,fullname,password,email,phoneNumber,storeName) VALUES(?,?,?,?,?,?)";
        try(Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query)){

            pstmt.setString(1,username);
            pstmt.setString(2,fullname);
            pstmt.setString(3,password);
            pstmt.setString(4,email);
            pstmt.setString(5,phoneNumber);
            pstmt.setString(6,storeName);



            // why execute update and why not query? because we are updating values in the database
            // not asking for a query
            pstmt.executeUpdate();
            System.out.println("SUCCESSFULLY CREATED USER");
            return true;

        } catch (SQLException e) {
            System.err.println("FAILED CREATING USER");
            return false;
        }

    }

}
