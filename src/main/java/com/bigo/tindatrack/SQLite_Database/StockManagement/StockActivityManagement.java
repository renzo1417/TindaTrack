package com.bigo.tindatrack.SQLite_Database.StockManagement;

import java.sql.*;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class StockActivityManagement {
    public static int addActivity(String name, int oldQty, int newQty, String reason, String date, int owner_id) {
        String query = "INSERT INTO stockactivity(name, oldQty, newQty, reason, date, owner_id) VALUES (?,?,?,?,?,?)";

        try (Connection connected = connect();
             PreparedStatement pstmt = connected.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, oldQty);
            pstmt.setInt(3, newQty);
            pstmt.setString(4, reason);
            pstmt.setString(5,date);
            pstmt.setInt(6,owner_id);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
