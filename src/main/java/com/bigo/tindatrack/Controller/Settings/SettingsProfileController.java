package com.bigo.tindatrack.Controller.Settings;

import com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager;
import com.bigo.tindatrack.SQLite_Database.userManagement.UserUIHelper;
import com.bigo.tindatrack.data.models.User;
import com.bigo.tindatrack.utils.utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;

public class SettingsProfileController {

    @FXML
    private TextField fullName, username, emailAddress;
    @FXML
    private Label username_top, username_bottom, username_top_initial, username_bottom_initial, username_middle, username_middle_initial;
    private User user;

    @FXML
    //initialazation
    public void initialize() {
        User user = loadUser();

        if (user == null) {
            System.out.println("Error: No user found!");
            return;
        }
        UserUIHelper.setupUserUI(username_top_initial,
                username_bottom_initial,
                username_top,
                username_bottom,
                loadUser());

        username_middle.setText(loadUser().getUsername());
        String firstLetter = user.getUsername().charAt(0) + "";
        username_middle_initial.setText(" " + firstLetter.toUpperCase());
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

    public void onNotificationIconClick(MouseEvent mouseEvent) {
        utility.switchToNotification(mouseEvent);
    }

    // switches screen using util
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

    public void goToInsight(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Insights-view.fxml");
    }

}