package com.bigo.tindatrack.SQLite_Database.userManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.bigo.tindatrack.SQLite_Database.TableManagement.connect;

public class AuthenticateUser {

    public static boolean authenticateUser(String username, String password){
        String query = "SELECT id FROM users WHERE username = ? AND password = ?";
        try(Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2,password);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.println("SUCCESSFULLY LOGGED IN FOR " + username);
                return true;
            }else{
                System.err.println("INVALID USERNAME OR PASSWORD FOR " + username);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("ERROR CONNECTION DURING LOGIN");
            return false;
        }

    }

}
