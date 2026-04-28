package com.bigo.tindatrack.Product;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;

public class Status {
    private String status;

    private ProductStatusController controller;

    public Status() {
        try {
            FXMLLoader loader = new FXMLLoader(Product.class.getResource("/com/bigo/tindatrack/Status-view.fxml"));

            Parent root = loader.load();

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pane getStatusPane() {
        return controller.getStatusPane();
    }

    public void updateStatus(LocalDate expiryDate, int quantity, int originalQuantity) {
        String labelColor = "#25A14B";
        String paneColor = "#DDFBE5";
        String circleColor = "#25A14B";
        String currentStatus = "Safe";

        LocalDate today = LocalDate.now();

        if (expiryDate != null && expiryDate.isBefore(today)) {
            paneColor = "#FCDFE1";
            labelColor = "#D11A16";
            circleColor = "#D11A16";
            currentStatus = "Expired";
        }
        else if (expiryDate != null && (expiryDate.isBefore(today.plusDays(7)) || expiryDate.isEqual(today))) {
            paneColor = "#FDF3C5";
            labelColor = "#DC8301";
            circleColor = "#DC8301";
            currentStatus = "Near Expiry";
        }
        else if (quantity <= (originalQuantity * 0.25)) {
            paneColor = "#FEF0E6";
            labelColor = "#8E4400";
            circleColor = "#8E4400";
            currentStatus = "Low Stock";
        }

        setStatus(currentStatus);

        if (controller != null) {
            controller.getStatusPane().setStyle("-fx-background-color: " + paneColor + "; -fx-background-radius: 15;");
            controller.getStatusColor().setStyle("-fx-fill: " + circleColor + ";");
            controller.getStatusLabel().setStyle("-fx-text-fill: " + labelColor + ";");
            controller.getStatusLabel().setText(getStatus());
        }
    }
}
