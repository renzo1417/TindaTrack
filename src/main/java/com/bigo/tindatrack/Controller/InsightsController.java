package com.bigo.tindatrack.Controller;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.data.InventoryList.InventoryList;
import com.bigo.tindatrack.data.StockDetails.StockDetails;
import com.bigo.tindatrack.data.StockDetails.StockDetailsList;
import com.bigo.tindatrack.utils.utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;


public class InsightsController {
    private InventoryList inventoryList = new InventoryList();
    private StockDetailsList detailsList = new StockDetailsList();

    // ------------ Recommendation Fields - Densing ------------
    // Recommendation Product Names
    @FXML
    private Label reco_ProductName_1, reco_ProductName_2, reco_ProductName_3;
    @FXML
    private Label reco_ProductName_4, reco_ProductName_5, reco_ProductName_6;
    @FXML
    private Label reco_ProductName_7, reco_ProductName_8, reco_ProductName_9;
    @FXML
    private Label reco_ProductName_10;

    // Recommendation Categories
    @FXML
    private Label reco_Category_1, reco_Category_2, reco_Category_3;
    @FXML
    private Label reco_Category_4, reco_Category_5, reco_Category_6;
    @FXML
    private Label reco_Category_7, reco_Category_8, reco_Category_9;
    @FXML
    private Label reco_Category_10;

    // Recommendation Suggestions
    @FXML
    private Label reco_Suggestion_1, reco_Suggestion_2, reco_Suggestion_3;
    @FXML
    private Label reco_Suggestion_4, reco_Suggestion_5, reco_Suggestion_6;
    @FXML
    private Label reco_Suggestion_7, reco_Suggestion_8, reco_Suggestion_9;
    @FXML
    private Label reco_Suggestion_10;

    private Label[] recommendationProductNames;
    private Label[] recommendationCategories;
    private Label[] recommendationSuggestion;

    @FXML
    public void initialize() {
        recommendationProductNames = new Label[]{
                reco_ProductName_1, reco_ProductName_2, reco_ProductName_3,
                reco_ProductName_4, reco_ProductName_5, reco_ProductName_6,
                reco_ProductName_7, reco_ProductName_8, reco_ProductName_9,
                reco_ProductName_10
        };

        recommendationCategories = new Label[]{
                reco_Category_1, reco_Category_2, reco_Category_3,
                reco_Category_4, reco_Category_5, reco_Category_6,
                reco_Category_7, reco_Category_8, reco_Category_9,
                reco_Category_10
        };

        recommendationSuggestion = new Label[]{
                reco_Suggestion_1, reco_Suggestion_2, reco_Suggestion_3,
                reco_Suggestion_4, reco_Suggestion_5, reco_Suggestion_6,
                reco_Suggestion_7, reco_Suggestion_8, reco_Suggestion_9,
                reco_Suggestion_10
        };

        for (int i = 0 ; i < 10 ; i++) {
            recommendationProductNames[i].setVisible(false);
            recommendationCategories[i].setVisible(false);
            recommendationSuggestion[i].setVisible(false);
        }

        updateRecommendations();
    }

    // ------------ Recommendation Methods- DENSING ------------

    private static class RecommendationInfo {
        private String productName;
        private String category;
        private String suggestion;

        private RecommendationInfo(String productName, String category, String suggestion) {
            this.productName = productName;
            this.category = category;
            this.suggestion = suggestion;
        }

        public String getProductName() {
            return productName;
        }

        public String getCategory() {
            return category;
        }

        public String getSuggestion() {
            return suggestion;
        }
    }

    public void updateRecommendations() {
        List<RecommendationInfo> infos = new ArrayList<>();

        // sell first is priority #1
        checkForSellFirst(infos);
        checkForRestock(infos);
        checkForOverStock(infos);

        for (int i = 0 ; i < infos.size() ; i++) {
            recommendationProductNames[i].setVisible(true);
            recommendationCategories[i].setVisible(true);
            recommendationSuggestion[i].setVisible(true);

            recommendationProductNames[i].setText(infos.get(i).getProductName());
            recommendationCategories[i].setText(infos.get(i).getCategory());
            recommendationSuggestion[i].setText(infos.get(i).getSuggestion());

            String textColor;
            String backgroundColor;

            if (infos.get(i).getSuggestion().equals("Restock Soon")) {
                textColor = "#3182CE";
                backgroundColor = "#E6F0FF";
            } else if (infos.get(i).getSuggestion().equals("Sell First")) {
                textColor = "#f97316";
                backgroundColor = "#FFF7ED";
            } else {
                textColor = "#38A169";
                backgroundColor = "#E6FFFA";
            }

            recommendationSuggestion[i].setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: " + textColor + "; -fx-padding: 6 12; -fx-background-radius: 15");
        }

    }

    private void checkForRestock(List<RecommendationInfo> infos) {
        for (Product p : inventoryList.getProductList()) {
            if (p.getQuantity() > 0 && (
                    (p.getQuantity() <= 0.10 * p.getOriginalQuantity()) ||
                            (p.getOriginalQuantity() <= 10 && p.getQuantity() < 5)
            )) {
                if (infos.size() < 10) {
                    infos.add(createInfoRecommendation(p.getProductName(), p.getCategory(), "Restock Soon"));
                }
            }
        }
    }

    private void checkForSellFirst(List<RecommendationInfo> infos) {
        LocalDate today = LocalDate.now();
        for (Product p : inventoryList.getProductList()) {
            long daysRemaining = ChronoUnit.DAYS.between(today, p.getLocalExpiryDate());

            if (daysRemaining <= 3 && daysRemaining >= 0) {
                if (infos.size() < 10) {
                    infos.add(createInfoRecommendation(p.getProductName(), p.getCategory(), "Sell First"));
                }
            }
        }
    }

    private void checkForOverStock(List<RecommendationInfo> infos) {
        // 1. Group logs by Product Name to analyze history per item
        Map<String, List<StockDetails>> logsByProduct = new HashMap<>();
        for (StockDetails detail : detailsList.getDetailsList()) {
            logsByProduct.computeIfAbsent(detail.getProductName(), k -> new ArrayList<>()).add(detail);
        }

        for (Map.Entry<String, List<StockDetails>> entry : logsByProduct.entrySet()) {
            // Stop if we've already reached the limit of 10 recommendations
            if (infos.size() >= 10) break;

            String name = entry.getKey();
            List<StockDetails> history = entry.getValue();

            // Ensure chronological order
            history.sort(Comparator.comparing(d -> LocalDate.parse(d.getDate())));

            if (history.size() < 2) continue;

            // 2. Identify the two most recent "Stock In" events
            StockDetails latestRestock = null;
            StockDetails previousRestock = null;
            int latestIdx = -1;
            int previousIdx = -1;

            for (int i = history.size() - 1; i >= 0; i--) {
                StockDetails current = history.get(i);
                // Check for quantity increase
                if (current.getNewQty() > current.getOldQty()) {
                    if (latestRestock == null) {
                        latestRestock = current;
                        latestIdx = i;
                    } else {
                        previousRestock = current;
                        previousIdx = i;
                        break;
                    }
                }
            }

            // 3. Perform Analysis if two restock points exist
            if (latestRestock != null && previousRestock != null) {
                int totalSold = 0;
                // Sum sales logs between the restocks
                for (int j = previousIdx + 1; j < latestIdx; j++) {
                    StockDetails log = history.get(j);
                    if (log.getNewQty() < log.getOldQty()) {
                        totalSold += (log.getOldQty() - log.getNewQty());
                    }
                }

                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(
                        LocalDate.parse(previousRestock.getDate()),
                        LocalDate.parse(latestRestock.getDate())
                );

                // 4. Criteria: 1.5x growth, 30+ days gap, and low sales (< 10% of previous stock)
                if (latestRestock.getNewQty() >= previousRestock.getOldQty() * 1.5
                        && daysBetween >= 30
                        && totalSold < (previousRestock.getNewQty() * 0.10)) {

                    // 5. Priority Check: Only add if product isn't already flagged for something more urgent
                    boolean alreadyFlagged = false;
                    for (RecommendationInfo info : infos) {
                        if (info.productName.equalsIgnoreCase(name)) {
                            alreadyFlagged = true;
                            break;
                        }
                    }

                    if (!alreadyFlagged && infos.size() < 10) {
                        // Fetch category (placeholder logic—replace with your actual category source)
                        String category = "General";

                        // Strictly suggest only "Overstocked" as requested
                        infos.add(createInfoRecommendation(name, category, "Overstocked"));
                    }
                }
            }
        }
    }

    private RecommendationInfo createInfoRecommendation(String name, String category, String suggestion) {
        return new RecommendationInfo(name, category, suggestion);
    }

    // ------------ NAVIGATION METHODS ----------------

    public void goToInventory(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml");
    }

    public void goToStockActivity(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/StockActivity-view.fxml");
    }

    public void goToSetting(ActionEvent event) {
        System.out.println("setting button");
        utility.switchScene(event, "/com/bigo/tindatrack/SettingsMarket-view.fxml");
    }

    public void goTovVewAllerts(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml");
    }

    public void goToDashboard(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml");
    }

    public void goToNotifications(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Notification-view.fxml");
    }
}
