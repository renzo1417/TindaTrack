package com.bigo.tindatrack.Product;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.time.LocalDate;

public class Product {
    private int    id;
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getOriginalQuantity() { return originalQuantity; }

    public String getExpiryDate() {
        if (expiryDate == null) {
            return "Non-perishable";
        }

        String date = expiryDate.toString();
        return date;
    }

    public LocalDate getLocalExpiryDate() {
        return expiryDate;
    }

    public String getCategory() {
        return category;
    }

    public Pane getStatus() {
        return status.getStatusPane();
    }

    public Status getStatusController() {
        return status;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOriginalQuantity(int originalQuantity) {
        if (originalQuantity > this.originalQuantity) {
            this.originalQuantity = originalQuantity;
        }
    }
}
