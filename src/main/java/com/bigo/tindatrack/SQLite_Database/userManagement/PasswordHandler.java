package com.bigo.tindatrack.SQLite_Database.userManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.bigo.tindatrack.SQLite_Database.TableManagement.connect;

public class PasswordHandler {

    String userForChange = null;


    // first version of this was a basic boolean function where it will return true and false but i thought that it
    // wouldnt work well

    // i used username since it will be a unique value instead of fullname
    //so I made the verifyUser return a username String

    // added verify user inside password handler because this block will only be mostly used when verifying before
    //changing user password
    public static String verifyUser(String fullname, String phoneNumber, String storeName){
        String query = "SELECT username FROM users WHERE fullname = ? AND phoneNumber = ? AND storeName = ?";
        try(Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query)){

            //for testing purps
            System.out.println("fullname inputted: " + fullname);
            System.out.println("phoneNumber inputted: " + phoneNumber);
            System.out.println("storeName inputted: " + storeName);

            pstmt.setString(1, fullname);
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, storeName);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                String usernameFromDB = rs.getString("username");
                System.out.println("User Verified : " + usernameFromDB);
                return usernameFromDB;
            }else{
                System.err.println("NO MATCHING USER FOUND!");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("ERROR CONNECTING");
            return null;
        }

    }

    public static boolean changePass(String username, String newPassword){
        String query = "UPDATE users SET password = ? WHERE username = ?";
        try(Connection connected = connect(); PreparedStatement pstmt = connected.prepareStatement(query)){

            pstmt.setString(1,newPassword);
            pstmt.setString(2,username);

            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.err.println("ERROR CONNECTING");
            return false;
        }

    }

}

