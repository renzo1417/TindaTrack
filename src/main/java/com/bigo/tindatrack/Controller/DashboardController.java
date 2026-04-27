package com.bigo.tindatrack.Controller;

import com.bigo.tindatrack.data.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;


import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;
import static com.bigo.tindatrack.SQLite_Database.userManagement.UserService.getUser;

public class DashboardController {

    @FXML
    private Label welcomeField, username_top, username_bottom, dateField;
    @FXML
    private Button inventoryButton, insightButton, stockactivityButton, settingButton, viewAllerts;

    private User user = loadUser();

    public void initialize(){
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

    //helper for the switch screens
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            Stage stage = (Stage) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading scene: " + fxmlPath);
            e.printStackTrace();
        }
    }


    // These are all the buttons for the dashboard and this includes the logout button that resets the SessionManager for user
    @FXML
    public void goToInventoryButton(ActionEvent event){
                    switchScene(event,"/com/bigo/tindatrack/Inventory-view.fxml" );
    }
    @FXML
    public void goToInsightButton(ActionEvent event ){
                    switchScene(event, "/com/bigo/tindatrack/insight-view.fxml");
    }
    @FXML
    public void goToStockactivityButton(ActionEvent event){
                switchScene(event, "/com/bigo/tindatrack/Stocksactivity-view.fxml");
    }
    @FXML
    public void goToSettingButton(ActionEvent event){
        switchScene(event, "/com/bigo/tindatrack/Settings-view.fxml");
    }
    @FXML
    public void gotoviewAllerts(ActionEvent event){
        switchScene(event,"/com/bigo/tindatrack/Inventory-view.fxml" );
    }
    @FXML
    public void goThreeDashboard(ActionEvent event){
        switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml");
    }
    @FXML
    public void setLogout(ActionEvent event) {
        com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.clearSession();
        switchScene(event, "/com/bigo/tindatrack/Login-view.fxml");
    }








}
