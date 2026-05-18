package com.bigo.tindatrack.Controller.Insights;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.userManagement.UserUIHelper;
import com.bigo.tindatrack.data.InventoryList.InventoryList;
import com.bigo.tindatrack.data.StockDetails.StockDetails;
import com.bigo.tindatrack.data.StockDetails.StockDetailsList;
import com.bigo.tindatrack.utils.utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;
import static com.bigo.tindatrack.utils.utility.switchScene;


public class InsightsController {
    private InventoryList inventoryList = new InventoryList();
    private StockDetailsList detailsList = new StockDetailsList();
    // Slow Moving Items
    @FXML Label slowMoving_category1, slowMoving_category2, slowMoving_category3, slowMoving_category4, slowMoving_category5;
    @FXML Label slowMoving_Type1, slowMoving_Type2, slowMoving_Type3, slowMoving_Type4, slowMoving_Type5;
    @FXML Label slowMoving_count1, slowMoving_count2, slowMoving_count3, slowMoving_count4, slowMoving_count5;
    private SlowMovingItemsController slowMovingController;
    // Fast Moving Items
    @FXML ProgressBar item1PB, item2PB, item3PB, item4PB, item5PB;
    @FXML Label item1Label, item2Label, item3Label, item4Label, item5Label;
    // under Fast Moving Items
    @FXML Label item1FastLabel, item2FastLabel, item3FastLabel, item4FastLabel;
    @FXML Label item1_count_Label, item2_count_Label, item3_count_Label, item4_count_Label;
    @FXML private Label username_top, username_bottom, username_top_initial, username_bottom_initial;



    @FXML private GridPane expiryGrid;

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

        // slowlyMovingItems
        // Label
        slowMovingController = new SlowMovingItemsController(
                new Label[]{
                        slowMoving_Type1, slowMoving_Type2, slowMoving_Type3,
                        slowMoving_Type4, slowMoving_Type5
                },
                new Label[]{
                        slowMoving_category1, slowMoving_category2, slowMoving_category3,
                        slowMoving_category4, slowMoving_category5
                },
                new Label[]{
                        slowMoving_count1, slowMoving_count2, slowMoving_count3,
                        slowMoving_count4, slowMoving_count5
                }
        );
        slowMovingController.load();

        // Fast Moving Items
        // Fast Moving Items
        FastMovingItemsController fastMovingController = new FastMovingItemsController(
                new ProgressBar[]{ item1PB, item2PB, item3PB, item4PB, item5PB },
                new Label[]{ item1Label, item2Label, item3Label, item4Label, item5Label },
                new Label[]{ item1FastLabel, item2FastLabel, item3FastLabel, item4FastLabel },
                new Label[]{ item1_count_Label, item2_count_Label, item3_count_Label, item4_count_Label }
        );
        fastMovingController.load();


        InsightsExpiryController expiryController = new InsightsExpiryController();
        expiryController.setExpiryGrid(expiryGrid); // we pass the grid directly
        expiryController.loadExpiryGrid();

        UserUIHelper.setupUserUI(username_top_initial,
                username_bottom_initial,
                username_top,
                username_bottom,
                loadUser());

        updateRecommendations();
        loadExpirySection();
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
    private void loadExpirySection() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource(
                            "/com/bigo/tindatrack/Insights-Expiry-view.fxml")
            );
            loader.load(); // loads and triggers InsightsExpiryController.initialize()
        } catch (Exception e) {
            System.err.println("Expiry section load error: " + e.getMessage());
        }
    }

    private void checkForRestock(List<RecommendationInfo> infos) {
        if (inventoryList.getProductList().isEmpty()) {
            inventoryList.loadItems();
        }
        for (Product p : inventoryList.getProductList()) {
            if (infos.size() >= 10) break;

            if (isAlreadyFlagged(infos, p.getProductName())) {
                continue;
            }

            if (p.getQuantity() > 0 && (
                    (p.getQuantity() <= 0.10 * p.getOriginalQuantity()) ||
                            (p.getOriginalQuantity() <= 10 && p.getQuantity() < 5)
            )) {
                infos.add(createInfoRecommendation(p.getProductName(), p.getCategory(), "Restock Soon"));
            }
        }
    }

    private void checkForSellFirst(List<RecommendationInfo> infos) {
        if (inventoryList.getProductList().isEmpty()) {
            inventoryList.loadItems();
        }
        LocalDate today = LocalDate.now();
        for (Product p : inventoryList.getProductList()) {
            if (infos.size() >= 10) break;

            long daysRemaining = ChronoUnit.DAYS.between(today, p.getLocalExpiryDate());

            if (daysRemaining <= 3 && daysRemaining >= 0) {
                infos.add(createInfoRecommendation(p.getProductName(), p.getCategory(), "Sell First"));
            }
        }
    }

    private void checkForOverStock(List<RecommendationInfo> infos) {
        Map<String, List<StockDetails>> logsByProduct = new HashMap<>();
        for (StockDetails detail : detailsList.getDetailsList()) {
            logsByProduct.computeIfAbsent(detail.getProductName(), k -> new ArrayList<>()).add(detail);
        }

        for (Map.Entry<String, List<StockDetails>> entry : logsByProduct.entrySet()) {
            if (infos.size() >= 10) break;

            String name = entry.getKey();

            if (isAlreadyFlagged(infos, name)) {
                continue;
            }

            List<StockDetails> history = entry.getValue();

            history.sort(Comparator.comparing(d -> LocalDate.parse(d.getDate())));

            if (history.size() < 2) continue;

            StockDetails latestRestock = null;
            StockDetails previousRestock = null;
            int latestIdx = -1;
            int previousIdx = -1;

            for (int i = history.size() - 1; i >= 0; i--) {
                StockDetails current = history.get(i);
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

                if (latestRestock.getNewQty() >= previousRestock.getOldQty() * 1.5
                        && daysBetween >= 30
                        && totalSold < (previousRestock.getNewQty() * 0.10)) {

                    String category = "General";

                    infos.add(createInfoRecommendation(name, category, "Overstocked"));
                }
            }
        }
    }

    // HELPER METHOD: Adds clean deduplication logic to keep code clean
    private boolean isAlreadyFlagged(List<RecommendationInfo> infos, String productName) {
        for (RecommendationInfo info : infos) {
            if (info.getProductName().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }

    private RecommendationInfo createInfoRecommendation(String name, String category, String suggestion) {
        return new RecommendationInfo(name, category, suggestion);
    }

    // ------------ NAVIGATION METHODS ----------------

    public void onNotificationIconClick(MouseEvent mouseEvent) {
        utility.switchToNotification(mouseEvent);
    }

    public void goToInventory(ActionEvent event) {
        switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml");
    }

    public void goToStockActivity(ActionEvent event) {
        switchScene(event, "/com/bigo/tindatrack/StockActivity-view.fxml");
    }

    public void goToSetting(ActionEvent event) {
        System.out.println("setting button");
        switchScene(event, "/com/bigo/tindatrack/SettingsMarket-view.fxml");
    }

    public void goToDashboard(ActionEvent event) {
        switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml");
    }

    public void goToNotifications(ActionEvent event) {
        switchScene(event, "/com/bigo/tindatrack/Notification-view.fxml");
    }

    public void setInsightsLogout(ActionEvent event) {
        com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.clearSession();
        switchScene(event, "/com/bigo/tindatrack/Login-view.fxml");
    }
}
