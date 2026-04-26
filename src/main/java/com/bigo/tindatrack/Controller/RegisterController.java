package com.bigo.tindatrack.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static com.bigo.tindatrack.SQLite_Database.userManagement.CreateUser.createUser;

public class RegisterController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField storeNameField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField confirmPasswordField;
    @FXML private Button createAccountButton;
    @FXML private CheckBox agreeCheckBox;
    @FXML private Label signInLabel;


    public void handleSignInButton(ActionEvent event){

    }

    @FXML
    public void setCreateAccountButton(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullname = fullNameField.getText();
        String email = emailField.getText();
        String storeName = storeNameField.getText();
        String phoneNumber = phoneField.getText();
        String confirmPass = confirmPasswordField.getText();

        // 1username, 2fullname, 3password, 4email, 5phoneNumber, 6storeName
        createUser(username, fullname, password,email,phoneNumber,storeName);

    }


}
