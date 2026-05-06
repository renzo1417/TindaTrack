package com.bigo.tindatrack.Controller.Settings;

import com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager;
import com.bigo.tindatrack.data.models.User;
import com.bigo.tindatrack.utils.utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SettingsProfileController {

    @FXML
    private TextField fullName, username, emailAddress;


    @FXML
    public void initialize() {
        displayUserData();
    }

    private void displayUserData() {
        User currentUser = SessionManager.loadUser();
        if (currentUser != null) {
            fullName.setText(currentUser.getFullname());
            username.setText(currentUser.getUsername());
            emailAddress.setText(currentUser.getEmail());
        } else {
            System.out.println("No active session found.");
        }
    }

    @FXML
    private void goToDashboard(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml");
    }

    @FXML
    private void setSettingsLogout(ActionEvent event) {
        SessionManager.clearSession();
        utility.switchScene(event, "/com/bigo/tindatrack/Login-view.fxml");
    }

    @FXML
    private void goToProfileSettings(ActionEvent event){
        utility.switchScene(event, "/com/bigo/tindatrack/SettingsProfile-view.fxml");
    }

    @FXML
    private void goToStoreInfo(ActionEvent event){
        utility.switchScene(event, "/com/bigo/tindatrack/SettingsMarket-view.fxml");
    }

    @FXML
    private void goToNotifications(ActionEvent event){
        utility.switchScene(event, "/com/bigo/tindatrack/Notification-view.fxml");
    }

    @FXML
    private void goToNotificationsSettings(ActionEvent event){
        utility.switchScene(event, "/com/bigo/tindatrack/SettingsNotification-view.fxml");
    }

    @FXML
    private void goToStockActivity(ActionEvent event){
        utility.switchScene(event, "/com/bigo/tindatrack/StockActivity-view.fxml");
    }

    @FXML
    private void goToInventoryActivity(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml");
    }
}