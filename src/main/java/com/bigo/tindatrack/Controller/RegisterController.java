package com.bigo.tindatrack.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleSignInButton(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String fullname = fullNameField.getText();
        String email = emailField.getText();
        String storeName = storeNameField.getText();
        String phoneNumber = phoneField.getText();
        String confirmPass = confirmPasswordField.getText();

        //check if fields are empty
        if(fullname.isEmpty()){

            showAlert("FULLNAME CANNOT BE EMPTY");
        }

        else if(email.isEmpty()){

            showAlert("EMAIL CANNOT BE EMPTY");
        }

        else if(phoneNumber.isEmpty()){

            showAlert("PHONE NUMBER CANNOT BE EMPTY");
        }

        else if (!phoneNumber.matches("\\d+")) {
            showAlert("PHONE NUMBER MUST CONTAIN ONLY DIGITS");
        }

        else if (phoneNumber.length() != 11) {
            showAlert("PHONE NUMBER MUST BE EXACTLY 11 DIGITS");
        }

        else if (!phoneNumber.startsWith("09")) {
            showAlert("PHONE NUMBER MUST START WITH 09");
        }

        else if(storeName.isEmpty()){

            showAlert("STORE NAME CANNOT BE  EMPTY");
        }

        else if(username.isEmpty()){

            showAlert("USERNAME CANNOT BE EMPTY");
        }

        else if(password.isEmpty()){

            showAlert("PASSWORD CANNOT BE EMPTY");

        }

        else if(confirmPass.isEmpty()){

            showAlert("CONFIRM PASSWORD CANNOT BE  EMPTY");
        }

        else if(!confirmPass.equals(password)){
            showAlert("Passwords don't match");
        }

        else{

            Alert success = new Alert(Alert.AlertType.WARNING);
            success.setTitle("SUCCESS");
            success.setHeaderText(null);
            success.setContentText("Account created successfully!");
            success.showAndWait();

            System.out.println("user created successfully!");
            createUser(username, fullname, password,email,phoneNumber,storeName);

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/com/bigo/tindatrack/Login-view.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        }



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
//        createUser(username, fullname, password,email,phoneNumber,storeName);

        System.out.println(username  + "\n" +  password + fullname + "\n" + email + "\n" + storeName + "\n" + phoneNumber + "\n" + confirmPass);



    }


    public void onsigninClick(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/bigo/tindatrack/Login-view.fxml"));

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();

    }
}
