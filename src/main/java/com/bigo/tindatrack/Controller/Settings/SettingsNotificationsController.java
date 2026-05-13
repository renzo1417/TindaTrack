package com.bigo.tindatrack.Controller.Settings;

import com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager;
import com.bigo.tindatrack.data.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;

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
    private Slider editorProgress;
    @FXML
    private Label username_top, username_bottom;
    private User user;

    public void initialize(){
        user = SessionManager.loadUser();

        if (user == null) {
            System.out.println("Error: No user found!");
            return;
        }

        username_top.setText(user.getUsername());
        username_bottom.setText(user.getUsername());

    }



    public void goToProfileSettings(ActionEvent event) {

    }
    public void setSettingsLogout(ActionEvent event) {

    }
    public void goToNotifications(ActionEvent event) {

    }
    public void goToStockActivity(ActionEvent event) {

    }
    public void goToInsights(ActionEvent event) {

    }
    public void goToInventory(ActionEvent event) {

    }
    public void goToDashboard(ActionEvent event) {

    }
    public void goToStoreInfo(ActionEvent event) {

    }

    public void goToNotificationsSettings(ActionEvent event){

    }

}
