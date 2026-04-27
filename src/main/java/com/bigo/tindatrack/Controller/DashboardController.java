package com.bigo.tindatrack.Controller;

import com.bigo.tindatrack.data.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.time.format.TextStyle;


import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;
import static com.bigo.tindatrack.SQLite_Database.userManagement.UserService.getUser;

public class DashboardController {

    @FXML
    private Label welcomeField, username_top, username_bottom, dateField;


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

}
