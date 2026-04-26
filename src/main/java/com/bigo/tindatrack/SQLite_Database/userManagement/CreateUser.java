package com.bigo.tindatrack.SQLite_Database.userManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.bigo.tindatrack.SQLite_Database.TableManagement.connect;

public class CreateUser {

    public static boolean createUser(String username, String password){
        String query = "INSERT INTO users(username,password) VALUES(?,?)";
        try(Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query)){

            pstmt.setString(1,username);
            pstmt.setString(2,password);

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
