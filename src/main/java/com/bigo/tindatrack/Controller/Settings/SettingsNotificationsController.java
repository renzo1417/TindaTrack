package com.bigo.tindatrack.Controller.Settings;

import com.bigo.tindatrack.SQLite_Database.NotificationManagement.NotificationPreferencesDAO;
import com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager;
import com.bigo.tindatrack.SQLite_Database.userManagement.UserUIHelper;
import com.bigo.tindatrack.data.models.NotificationPreferences;
import com.bigo.tindatrack.data.models.User;
import com.bigo.tindatrack.utils.utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;


public class SettingsNotificationsController {

    @FXML
    private ToggleButton TButton1;

    @FXML
    private ToggleButton TButton2;
    @FXML
    private ToggleButton TButton3;
    @FXML
    private ToggleButton TButton4;
    @FXML
    private ToggleButton TButton5;
    @FXML
    private Label username_top, username_bottom, username_top_initial, username_bottom_initial;
    private User user;
    private NotificationPreferences prefs;

    public void initialize(){
        user = SessionManager.loadUser();

        if (user == null) {
            System.out.println("Error: No user found!");
            return;
        }

//        username_top.setText(user.getUsername());
//        username_bottom.setText(user.getUsername());
        UserUIHelper.setupUserUI(username_top_initial,
                username_bottom_initial,
                username_top,
                username_bottom,
                loadUser());

        prefs = NotificationPreferencesDAO.load(user.getId());
        applyPrefsToToggles();
    }
    private void applyPrefsToToggles() {
        TButton1.setSelected(prefs.isExpiryAlerts());
        TButton2.setSelected(prefs.isLowStockAlerts());
        TButton3.setSelected(prefs.isRestockReminders());
        TButton4.setSelected(prefs.isNotificationSound());
        TButton5.setSelected(prefs.isEmailNotifications());
    }

    public void handleToggleB1(ActionEvent event){
        prefs.setExpiryAlerts(TButton1.isSelected());
    }
    public void handleToggleB2(ActionEvent event){
        prefs.setLowStockAlerts(TButton2.isSelected());
    }
    public void handleToggleB3(ActionEvent event){
        prefs.setRestockReminders(TButton3.isSelected());
    }
    public void handleToggleB4(ActionEvent event){
        prefs.setNotificationSound(TButton4.isSelected());
    }
    public void handleToggleB5(ActionEvent event){
        prefs.setEmailNotifications(TButton5.isSelected());
    }

    public void handleSaveSettings(ActionEvent event){
        prefs.setExpiryAlerts(TButton1.isSelected());
        prefs.setLowStockAlerts(TButton2.isSelected());
        prefs.setRestockReminders(TButton3.isSelected());
        prefs.setNotificationSound(TButton4.isSelected());
        prefs.setEmailNotifications(TButton5.isSelected());

        boolean success = NotificationPreferencesDAO.save(prefs);

        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(success ? "Settings Saved" : "Save Failed");
        alert.setHeaderText(null);
        alert.setContentText(success
                ? "Your notification preferences have been saved!"
                : "Something went wrong. Please try again.");
        alert.showAndWait();
    }


    public void onNotificationIconClick(MouseEvent mouseEvent) {
        utility.switchToNotification(mouseEvent);
    }

    public void goToInventory(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml");
    }

    public void goToInsights(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Insights-view.fxml");
    }

    public void goToStockActivity(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/StockActivity-view.fxml");
    }

    public void goToSettingButton(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/SettingsMarket-view.fxml");
    }

    public void goToDashboard(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml");
    }

    public void goToNotifications(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Notification-view.fxml");
    }

    public void setSettingsLogout(ActionEvent event) {
        com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.clearSession();
        utility.switchScene(event, "/com/bigo/tindatrack/Login-view.fxml");
    }

    public void goToProfileSettings(ActionEvent event) {
        System.out.println("Going to Profile Settings");
        utility.switchScene(event,"/com/bigo/tindatrack/SettingsProfile-view.fxml");
    }

    public void goToNotificationsSettings(ActionEvent event) {
        System.out.println("Going to Notification Settings");
        utility.switchScene(event,"/com/bigo/tindatrack/SettingsNotification-view.fxml");
    }

    public void goToStoreInfo(ActionEvent event){
        System.out.println("Going to Notification Settings");
        utility.switchScene(event,"/com/bigo/tindatrack/SettingsMarket-view.fxml");
    }

}
