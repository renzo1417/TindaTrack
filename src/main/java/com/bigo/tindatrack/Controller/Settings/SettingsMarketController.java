package com.bigo.tindatrack.Controller.Settings;

import com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager;
import com.bigo.tindatrack.data.models.User;
import com.bigo.tindatrack.utils.utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class SettingsMarketController {

    @FXML
    private TextField storeNameTF, ownerNameTF, contactNumberTF, storeAddTF;


    public void initialize(){
        User user = SessionManager.loadUser();

        storeNameTF.setText(user.getStoreName());
        ownerNameTF.setText(user.getFullname());
        contactNumberTF.setText(user.getPhoneNumber());
        storeAddTF.setText(user.getEmail());

    }

    public void goToInventory(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml");
    }

    public void goToInsightButton(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/insight-view.fxml");
    }

    public void goToStockactivityButton(ActionEvent event) {
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





}
