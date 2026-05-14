package com.bigo.tindatrack.data.models;

public class NotificationPreferences {
    private int     userId;
    private boolean expiryAlerts;
    private boolean lowStockAlerts;
    private boolean restockReminders;
    private boolean notificationSound;
    private boolean emailNotifications;

    public NotificationPreferences(int userId,
                                   boolean expiryAlerts,
                                   boolean lowStockAlerts,
                                   boolean restockReminders,
                                   boolean notificationSound,
                                   boolean emailNotifications) {
        this.userId              = userId;
        this.expiryAlerts        = expiryAlerts;
        this.lowStockAlerts      = lowStockAlerts;
        this.restockReminders    = restockReminders;
        this.notificationSound   = notificationSound;
        this.emailNotifications  = emailNotifications;
    }

    // ── Getters ──────────────────────────────────────────────────────────
    public int     getUserId()             { return userId; }
    public boolean isExpiryAlerts()        { return expiryAlerts; }
    public boolean isLowStockAlerts()      { return lowStockAlerts; }
    public boolean isRestockReminders()    { return restockReminders; }
    public boolean isNotificationSound()   { return notificationSound; }
    public boolean isEmailNotifications()  { return emailNotifications; }

    // ── Setters ──────────────────────────────────────────────────────────
    public void setExpiryAlerts(boolean v)       { this.expiryAlerts = v; }
    public void setLowStockAlerts(boolean v)     { this.lowStockAlerts = v; }
    public void setRestockReminders(boolean v)   { this.restockReminders = v; }
    public void setNotificationSound(boolean v)  { this.notificationSound = v; }
    public void setEmailNotifications(boolean v) { this.emailNotifications = v; }
}
