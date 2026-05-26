package com.bigo.tindatrack.SQLite_Database.NotificationManagement;

import com.bigo.tindatrack.data.models.NotificationPreferences;
import com.bigo.tindatrack.SQLite_Database.ConnectionBridge;

import java.sql.*;
public class NotificationPreferencesDAO {
    public static void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS notification_preferences (
                    user_id               INTEGER PRIMARY KEY,
                    expiry_alerts         INTEGER NOT NULL DEFAULT 1,
                    low_stock_alerts      INTEGER NOT NULL DEFAULT 1,
                    restock_reminders     INTEGER NOT NULL DEFAULT 1,
                    notification_sound    INTEGER NOT NULL DEFAULT 1,
                    email_notifications   INTEGER NOT NULL DEFAULT 0
                );
                """;
        try (Connection conn = ConnectionBridge.connect();
             Statement  stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("createTable error: " + e.getMessage());
        }
    }

    public static NotificationPreferences load(int userId) {
        String sql = "SELECT * FROM notification_preferences WHERE user_id = ?";
        try (Connection conn = ConnectionBridge.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new NotificationPreferences(
                        userId,
                        rs.getInt("expiry_alerts")       == 1,
                        rs.getInt("low_stock_alerts")    == 1,
                        rs.getInt("restock_reminders")   == 1,
                        rs.getInt("notification_sound")  == 1,
                        rs.getInt("email_notifications") == 1
                );
            }
        } catch (SQLException e) {
            System.err.println("load error: " + e.getMessage());
        }

        return new NotificationPreferences(userId, true, true, true, true, false);
    }

    public static boolean save(NotificationPreferences prefs) {
        String sql = """
                INSERT INTO notification_preferences
                    (user_id, expiry_alerts, low_stock_alerts,
                     restock_reminders, notification_sound, email_notifications)
                VALUES (?, ?, ?, ?, ?, ?)
                ON CONFLICT(user_id) DO UPDATE SET
                    expiry_alerts       = excluded.expiry_alerts,
                    low_stock_alerts    = excluded.low_stock_alerts,
                    restock_reminders   = excluded.restock_reminders,
                    notification_sound  = excluded.notification_sound,
                    email_notifications = excluded.email_notifications;
                """;
        try (Connection conn = ConnectionBridge.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, prefs.getUserId());
            ps.setInt(2, prefs.isExpiryAlerts()       ? 1 : 0);
            ps.setInt(3, prefs.isLowStockAlerts()     ? 1 : 0);
            ps.setInt(4, prefs.isRestockReminders()   ? 1 : 0);
            ps.setInt(5, prefs.isNotificationSound()  ? 1 : 0);
            ps.setInt(6, prefs.isEmailNotifications() ? 1 : 0);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("save error: " + e.getMessage());
            return false;
        }
    }
}
