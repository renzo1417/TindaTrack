package com.bigo.tindatrack.Controller.Notification;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.NotificationManagement.NotificationDAO;
import com.bigo.tindatrack.SQLite_Database.NotificationManagement.NotificationPreferencesDAO;
import com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable;
import com.bigo.tindatrack.data.models.NotificationPreferences;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId;

public class NotificationService {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ── Called on app startup ─────────────────────────────────────────────
    public static void evaluateAllProducts() {
        int ownerId = getCurrentUserId();
        if (ownerId == -1) return;

        // Load this user's notification preferences once
        NotificationPreferences prefs = NotificationPreferencesDAO.load(ownerId);

        ObservableList<Product> products = fetchDataFromTable.getAllProducts(ownerId);
        for (Product p : products) {
            evaluateProduct(p, prefs);
        }
    }

    // ── Called after addProduct() succeeds ────────────────────────────────
    public static void onProductAdded(Product p) {
        int ownerId = getCurrentUserId();
        if (ownerId == -1) return;

        String ts = LocalDateTime.now().format(FMT);
        NotificationPreferences prefs = NotificationPreferencesDAO.load(ownerId);

        // INFO alert for new product is always inserted regardless of prefs
        NotificationDAO.insert(
                ownerId,
                p.getId(),
                "INFO",
                p.getProductName() + " has been added to inventory. Quantity: "
                        + p.getQuantity() + ".",
                ts
        );

        // Play sound if enabled
        if (prefs.isNotificationSound()) {
            NotificationSoundPlayer.play();
        }

        evaluateProduct(p, prefs);
    }

    // ── Called after editProduct() / restock succeeds ─────────────────────
    public static void onProductUpdated(Product p) {
        int ownerId = getCurrentUserId();
        if (ownerId == -1) return;

        NotificationPreferences prefs = NotificationPreferencesDAO.load(ownerId);
        // FIXED: Added ownerId to deleteByProductId
        NotificationDAO.deleteByProductId(p.getId(), ownerId);
        evaluateProduct(p, prefs);
    }

    // ── Called after removeProduct() succeeds ─────────────────────────────
    public static void onProductDeleted(int productId) {
        int ownerId = getCurrentUserId();
        if (ownerId == -1) return;
        // FIXED: Fetched ownerId and passed it here
        NotificationDAO.deleteByProductId(productId, ownerId);
    }

    // ── Core rule engine — now receives prefs ─────────────────────────────
    private static void evaluateProduct(Product p, NotificationPreferences prefs) {
        if (p.getId() <= 0) return;

        int    ownerId = getCurrentUserId();
        String ts      = LocalDateTime.now().format(FMT);
        String name    = p.getProductName();
        boolean soundPlayed = false;

        // ── Expiry rules (gated by expiryAlerts pref) ─────────────────────
        if (prefs.isExpiryAlerts()) {
            LocalDate expiry = p.getLocalExpiryDate();
            if (expiry != null) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expiry);

                if (daysLeft < 0) {
                    // FIXED: Added ownerId to exists()
                    if (!NotificationDAO.exists(ownerId, p.getId(), "CRITICAL")) {
                        NotificationDAO.insert(ownerId, p.getId(), "CRITICAL",
                                name + " has expired! Remove from stock immediately.", ts);
                        soundPlayed = true;

                        // Send email if enabled
                        if (prefs.isEmailNotifications()) {
                            NotificationEmailSender.send(
                                    ownerId,
                                    "⚠️ CRITICAL: " + name + " has expired!",
                                    name + " has expired and must be removed from stock immediately."
                            );
                        }
                    }
                } else if (daysLeft <= 1) {
                    // FIXED: Added ownerId to exists()
                    if (!NotificationDAO.exists(ownerId, p.getId(), "CRITICAL")) {
                        NotificationDAO.insert(ownerId, p.getId(), "CRITICAL",
                                name + " expires in 1 day. Use or sell first!", ts);
                        soundPlayed = true;

                        if (prefs.isEmailNotifications()) {
                            NotificationEmailSender.send(
                                    ownerId,
                                    "⚠️ CRITICAL: " + name + " expires tomorrow!",
                                    name + " expires in 1 day. Please use or sell it first."
                            );
                        }
                    }
                } else if (daysLeft <= 7) {
                    // FIXED: Added ownerId to exists()
                    if (!NotificationDAO.exists(ownerId, p.getId(), "WARNING")) {
                        NotificationDAO.insert(ownerId, p.getId(), "WARNING",
                                name + " is nearing expiry — " + daysLeft + " days remaining.", ts);
                        soundPlayed = true;

                        if (prefs.isEmailNotifications()) {
                            NotificationEmailSender.send(
                                    ownerId,
                                    "⚠️ WARNING: " + name + " is expiring soon",
                                    name + " has " + daysLeft + " days left before expiry."
                            );
                        }
                    }
                }
            }
        }

        // ── Stock rules (gated by lowStockAlerts + restockReminders) ──────
        int qty = p.getQuantity();

        if (qty <= 0 && prefs.isLowStockAlerts()) {
            // FIXED: Added ownerId to exists()
            if (!NotificationDAO.exists(ownerId, p.getId(), "CRITICAL")) {
                NotificationDAO.insert(ownerId, p.getId(), "CRITICAL",
                        name + " is out of stock!", ts);
                soundPlayed = true;

                if (prefs.isEmailNotifications()) {
                    NotificationEmailSender.send(
                            ownerId,
                            "⚠️ CRITICAL: " + name + " is out of stock!",
                            name + " has run out of stock. Please reorder immediately."
                    );
                }
            }
        } else if (qty < 10 && prefs.isLowStockAlerts()) {
            // FIXED: Added ownerId to exists()
            if (!NotificationDAO.exists(ownerId, p.getId(), "WARNING")) {
                NotificationDAO.insert(ownerId, p.getId(), "WARNING",
                        name + " stock is low (" + qty + " units). Consider restocking.", ts);
                soundPlayed = true;

                // Restock reminder email (uses restockReminders pref)
                if (prefs.isRestockReminders() && prefs.isEmailNotifications()) {
                    NotificationEmailSender.send(
                            ownerId,
                            "🔔 Restock Reminder: " + name,
                            name + " has only " + qty + " units left. Consider restocking soon."
                    );
                }
            }
        }

        // ── Play sound once per product evaluation if anything was inserted
        if (soundPlayed && prefs.isNotificationSound()) {
            NotificationSoundPlayer.play();
        }
    }
}