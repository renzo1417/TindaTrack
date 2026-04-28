package com.bigo.tindatrack.Product;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.time.LocalDate;

public class Product {
    private String productName;
    private int quantity;
    private LocalDate expiryDate;
    private String category;
    private Status status;

    private int originalQuantity;

    public Product(String productName, int quantity, LocalDate expiryDate, String category) {
        this.productName = productName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.category = category;
        this.originalQuantity = quantity;

        status = new Status();
        status.updateStatus(expiryDate, quantity, originalQuantity);
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getExpiryDate() {
        if (expiryDate == null) {
            return "Non-perishable";
        }

        String date = expiryDate.toString();
        return date;
    }

    public String getCategory() {
        return category;
    }

    public Pane getStatus() {
        return status.getStatusPane();
    }
}
