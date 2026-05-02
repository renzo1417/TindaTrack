package com.bigo.tindatrack.Controller;

import com.bigo.tindatrack.data.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;


import com.bigo.tindatrack.utils.utility;
import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;

public class DashboardController  {

    @FXML
    private Label welcomeField, username_top, username_bottom, dateField;
    @FXML
    private Button inventoryButton, insightButton, stockactivityButton, settingButton, viewAllerts;

    private User user = loadUser();

    public void initialize(){
        // para ni if walay user d siya ka direct sa dashboard
        if (user == null) {
            System.out.println("Error: No user found!");
            return;
        }

        welcomeField.setText("Hello " + user.getUsername() +"! - Here's your inventory overview");
        username_top.setText(String.valueOf(user.getUsername()));
        username_bottom.setText(String.valueOf(user.getUsername()));

        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String formatted = today.format(formatter);


        String dayName = LocalDate.now()
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        dateField.setText(dayName + ", " + formatted + " - ");

    }




   //  These are all the buttons for the dashboard and this includes the logout button that resets the SessionManager for user
    @FXML
    public void goToInventoryButton(ActionEvent event){
                    utility.switchScene(event,"/com/bigo/tindatrack/Inventory-view.fxml" );
    }
    @FXML
    public void goToInsightButton(ActionEvent event ){
        utility.switchScene(event, "/com/bigo/tindatrack/insight-view.fxml");
    }
    @FXML
    public void goToStockactivityButton(ActionEvent event){
        utility.switchScene(event, "/com/bigo/tindatrack/Stocksactivity-view.fxml");
    }
    @FXML
    public void goToSettingButton(ActionEvent event){
        utility.switchScene(event, "/com/bigo/tindatrack/Settings-view.fxml");
    }
    @FXML
    public void gotoviewAllerts(ActionEvent event){
        utility.switchScene(event,"/com/bigo/tindatrack/Inventory-view.fxml" );
    }
    @FXML
    public void goThreeDashboard(ActionEvent event){
        utility.switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml");
    }
    @FXML
    public void setLogout(ActionEvent event) {
        com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.clearSession();
        this.user = null;
        utility.switchScene(event, "/com/bigo/tindatrack/Login-view.fxml");
    }








}
