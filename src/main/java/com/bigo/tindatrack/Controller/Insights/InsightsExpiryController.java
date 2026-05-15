package com.bigo.tindatrack.Controller.Insights;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId;

public class InsightsExpiryController {

     private GridPane expiryGrid;

    @FXML
    public void loadExpiryGrid() {
        int ownerId = getCurrentUserId();
        if (ownerId == -1) return;

        LocalDate today = LocalDate.now();

        List<Product> atRisk = fetchDataFromTable.getAllProducts(ownerId)
                .stream()
                .filter(p -> {
                    LocalDate expiry = p.getLocalExpiryDate();
                    if (expiry == null) return false;
                    long daysLeft = ChronoUnit.DAYS.between(today, expiry);
                    return daysLeft >= 0 && daysLeft <= 10;
                })
                .sorted(Comparator.comparing(p ->
                        ChronoUnit.DAYS.between(today, p.getLocalExpiryDate())))
                .collect(Collectors.toList());

        expiryGrid.getChildren().clear();

        if (atRisk.isEmpty()) {
            showEmptyState();
            return;
        }

        int col = 0, row = 0;
        for (Product p : atRisk) {
            long daysLeft = ChronoUnit.DAYS.between(today, p.getLocalExpiryDate());
            expiryGrid.add(buildCard(p, daysLeft), col, row);
            col++;
            if (col == 3) { col = 0; row++; }
        }
    }
    public void setExpiryGrid(GridPane grid) {
        this.expiryGrid = grid;
    }

    private VBox buildCard(Product p, long daysLeft) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(12, 15, 12, 15));
        card.setStyle(
                "-fx-background-color: #FFFDF5;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #FEEBC8;" +
                        "-fx-border-radius: 8;"
        );

        // Product name
        Label name = new Label(p.getProductName());
        name.setTextFill(Color.web("#1A202C"));
        name.setFont(Font.font("System Bold", 13));

        // Category
        String cat = (p.getCategory() != null && !p.getCategory().isBlank())
                ? p.getCategory() : "—";
        Label category = new Label(cat);
        category.setTextFill(Color.web("#718096"));
        category.setFont(Font.font(11));

        // Days left
        String daysText = daysLeft == 0 ? "Expires today!" : daysLeft + "d left";
        String daysColor = daysLeft <= 3 ? "#E53E3E" : "#C05621";

        Label days = new Label(daysText);
        days.setTextFill(Color.web(daysColor));
        days.setFont(Font.font("System Bold", 12));

        card.getChildren().addAll(name, category, days);
        return card;
    }

    private void showEmptyState() {
        Label empty = new Label("🎉 No items expiring soon!");
        empty.setTextFill(Color.web("#718096"));
        empty.setFont(Font.font("Segoe UI", 14));
        expiryGrid.add(empty, 0, 0);
    }
}