package com.bigo.tindatrack.Controller.Notification;

public class NotificationItem {

    public enum Type { CRITICAL, WARNING, INFO }

    public int     id;
    public Type    type;
    public String  message;
    public String  timestamp;
    public boolean isRead;
    public int     productId;

    public NotificationItem(Type type, String message,
                            String timestamp, int productId) {
        this.type      = type;
        this.message   = message;
        this.timestamp = timestamp;
        this.productId = productId;
        this.isRead    = false;
    }
}