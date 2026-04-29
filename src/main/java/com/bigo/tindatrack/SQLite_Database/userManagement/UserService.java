package com.bigo.tindatrack.SQLite_Database.userManagement;

import com.bigo.tindatrack.data.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;


public class UserService {

    public static User getUser(String user, String pass){

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try(Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query)) {

            pstmt.setString(1, user);
            pstmt.setString(2,pass);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println("SUCCESSFULLY LOGGED IN FOR " + user);


                String username = rs.getString("username");
                String fullname = rs.getString("fullname");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                String storeName = rs.getString("storeName");

                User user_from_database = new User(username, fullname, password, email, phoneNumber, storeName);

                return user_from_database;


            }else{
                System.err.println("INVALID USERNAME OR PASSWORD FOR " + user);
                return null;
            }

        } catch (SQLException e) {
            System.err.println("ERROR CONNECTION DURING LOGIN");
            return null;
        }


    }
}
