package com.bigo.tindatrack.SQLite_Database.NotificationManagement;

import com.bigo.tindatrack.Controller.Notification.NotificationItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.bigo.tindatrack.SQLite_Database.ConnectionBridge.connect;

public class NotificationDAO {

    public static boolean insert(int ownerId, Integer productId, String type,
                                 String message, String timestamp) {
        String query =
                "INSERT INTO notifications (owner_id, product_id, type, message, timestamp, is_read)" +
                        " VALUES (?, ?, ?, ?, ?, 0)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, ownerId);
            if (productId != null) {
                ps.setInt(2, productId);
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            ps.setString(3, type);
            ps.setString(4, message);
            ps.setString(5, timestamp);

            ps.executeUpdate();
            System.out.println("NOTIFICATION ADDED FOR USER " + ownerId + ": [" + type + "] " + message);
            return true;

        } catch (SQLException e) {
            System.err.println("ERROR ADDING NOTIFICATION: " + e.getMessage());
            return false;
        }
    }

    public static List<NotificationItem> getAll(int ownerId) {
        List<NotificationItem> list = new ArrayList<>();
        String query =
                "SELECT id, product_id, type, message, timestamp, is_read" +
                        " FROM notifications WHERE owner_id = ? ORDER BY id DESC";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NotificationItem item = new NotificationItem(
                            NotificationItem.Type.valueOf(rs.getString("type")),
                            rs.getString("message"),
                            rs.getString("timestamp"),
                            rs.getInt("product_id")
                    );
                    item.id     = rs.getInt("id");
                    item.isRead = rs.getInt("is_read") == 1;
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR FETCHING NOTIFICATIONS: " + e.getMessage());
        }
        return list;
    }

    public static boolean markAsRead(int notificationId) {
        String query = "UPDATE notifications SET is_read = 1 WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("ERROR MARKING READ: " + e.getMessage());
            return false;
        }
    }


    public static boolean markAllAsRead(int ownerId) {
        String query = "UPDATE notifications SET is_read = 1 WHERE owner_id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ownerId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("ERROR MARKING ALL READ: " + e.getMessage());
            return false;
        }
    }

    // ── Delete notifications for a deleted product ────────────────────────
    public static boolean deleteByProductId(int productId) {
        String query = "DELETE FROM notifications WHERE product_id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("ERROR DELETING NOTIFICATIONS: " + e.getMessage());
            return false;
        }
    }

    // ── FIX: Check duplicate alerts matching product, type, AND user ───────
    public static boolean exists(int ownerId, int productId, String type) {
        String query =
                "SELECT COUNT(*) FROM notifications" +
                        " WHERE owner_id = ? AND product_id = ? AND type = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ownerId);
            ps.setInt(2, productId);
            ps.setString(3, type);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("ERROR CHECKING DUPLICATE: " + e.getMessage());
            return false;
        }
    }

    // ── FIX: Count unread notifications ONLY for this specific user ────────
    public static int countUnread(int ownerId) {
        String query = "SELECT COUNT(*) FROM notifications WHERE is_read = 0 AND owner_id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("ERROR COUNTING UNREAD: " + e.getMessage());
        }
        return 0;
    }
}