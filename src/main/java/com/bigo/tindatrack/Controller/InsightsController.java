package com.bigo.tindatrack.Controller;

import com.bigo.tindatrack.utils.utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class InsightsController {
    // ------------ Recommendation Fields - Densing ------------
    @FXML
    private Label Reco_ProductName_1, Reco_ProductName_2, Reco_ProductName_3;
    @FXML
    private Label Reco_ProductName_4, Reco_ProductName_5, Reco_ProductName_6;
    @FXML
    private Label Reco_ProductName_7, Reco_ProductName_8, Reco_ProductName_9;
    @FXML
    private Label Reco_ProductName_10;

    // Recommendation Categories - 3 per row
    @FXML
    private Label Reco_Category_1, Reco_Category_2, Reco_Category_3;
    @FXML
    private Label Reco_Category_4, Reco_Category_5, Reco_Category_6;
    @FXML
    private Label Reco_Category_7, Reco_Category_8, Reco_Category_9;
    @FXML
    private Label Reco_Category_10;

    // Recommendation Suggestion

    @FXML
    private Label Reco_Suggestion_1, Reco_Suggestion_2, Reco_Suggestion_3;
    @FXML
    private Label Reco_Suggestion_4, Reco_Suggestion_5, Reco_Suggestion_6;
    @FXML
    private Label Reco_Suggestion_7, Reco_Suggestion_8, Reco_Suggestion_9;
    @FXML
    private Label Reco_Suggestion_10;

    // ------------ Recommendation Methods- DENSING ------------

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
