package com.bigo.tindatrack.Controller.Notification;


import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.NotificationManagement.NotificationDAO;
import com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable;
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
        if (ownerId != -1) {
            ObservableList<Product> products = fetchDataFromTable.getAllProducts(ownerId);
            for (Product p : products) {
                evaluateProduct(p);
            }
        }
    }

    // ── Called after addProduct() succeeds ────────────────────────────────
    public static void onProductAdded(Product p) {
        String ts = LocalDateTime.now().format(FMT);
        int ownerId = getCurrentUserId();

        NotificationDAO.insert(
                ownerId,
                p.getId(),
                "INFO",
                p.getProductName() + " has been added to inventory. Quantity: "    // ← getProductName()
                        + p.getQuantity() + ".",
                ts
        );

        evaluateProduct(p);
    }

    // ── Called after editProduct() / restock succeeds ─────────────────────
    public static void onProductUpdated(Product p) {
        NotificationDAO.deleteByProductId(p.getId());
        evaluateProduct(p);
    }

    // ── Called after removeProduct() succeeds ─────────────────────────────
    public static void onProductDeleted(int productId) {
        NotificationDAO.deleteByProductId(productId);
    }

    // ── Core rule engine ──────────────────────────────────────────────────
    private static void evaluateProduct(Product p) {
        if (p.getId() <= 0) return;

        // 1. Fetch the ownerId so we can pass it to the DAO
        int ownerId = com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId();

        String    ts    = LocalDateTime.now().format(FMT);
        LocalDate today = LocalDate.now();
        String    name  = p.getProductName();

        // ── Expiry rules ──────────────────────────────────────────────────
        LocalDate expiry = p.getLocalExpiryDate();
        if (expiry != null) {
            long daysLeft = ChronoUnit.DAYS.between(today, expiry);

            if (daysLeft < 0) {
                if (!NotificationDAO.exists(p.getId(), "CRITICAL")) {
                    NotificationDAO.insert(ownerId, p.getId(), "CRITICAL",
                            name + " has expired! Remove from stock immediately.", ts);
                }
            } else if (daysLeft <= 1) {
                if (!NotificationDAO.exists(p.getId(), "CRITICAL")) {
                    NotificationDAO.insert(ownerId, p.getId(), "CRITICAL",
                            name + " expires in 1 day. Use or sell first!", ts);
                }
            } else if (daysLeft <= 7) {
                if (!NotificationDAO.exists(p.getId(), "WARNING")) {
                    NotificationDAO.insert(ownerId, p.getId(), "WARNING",
                            name + " is nearing expiry — " + daysLeft
                                    + " days remaining.", ts);
                }
            }
        }

        int qty = p.getQuantity();

        if (qty <= 0) {
            if (!NotificationDAO.exists(p.getId(), "CRITICAL")) {
                NotificationDAO.insert(ownerId, p.getId(), "CRITICAL",
                        name + " is out of stock!", ts);
            }
        } else if (qty < 10) {
            if (!NotificationDAO.exists(p.getId(), "WARNING")) {
                NotificationDAO.insert(ownerId, p.getId(), "WARNING",
                        name + " stock is low (" + qty
                                + " units). Consider restocking.", ts);
            }
        }
    }
}
