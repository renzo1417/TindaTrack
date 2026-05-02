package com.bigo.tindatrack.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import static com.bigo.tindatrack.SQLite_Database.userManagement.PasswordHandler.changePass;

public class ForgotPasswordVerificationTwoController {

    @FXML
    private TextField newPasswordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private Button savePasswordButton;

    @FXML
    private Label backLabel;

    @FXML
    private StackPane req1Icon, req2Icon, req3Icon, req4Icon;

    private String username;
    private boolean password_approved = false;

    private void showAlert(String message) {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);

        alert.setTitle("Invalid Password");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void updateIcon(StackPane icon, boolean valid) {
        if (valid) {
            icon.setStyle("-fx-background-color: #2e8b2e; -fx-border-color: #2e8b2e;");
        } else {
            icon.setStyle("-fx-background-color: #ff4d4f; -fx-border-color: #ff4d4f;");
        }
    }

    private String validatePassword(String password) {

        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter (A-Z)";
        }

        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one number (0-9)";
        }

        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            return "Password must contain at least one special character";
        }

        return null; //valid password
    }

    public void initialize(){

        newPasswordField.textProperty().addListener((obs, oldValue, text) -> {

            updateIcon(req1Icon, text.length() >= 8);
            updateIcon(req2Icon, text.matches(".*[A-Z].*"));
            updateIcon(req3Icon, text.matches(".*\\d.*"));
            updateIcon(req4Icon, text.matches(".*[^a-zA-Z0-9].*"));

        });

    }

    public void setUsername(String username){
        this.username = username;

        System.out.println("Username Received");

    }


    public void handleSaveNewPassword(ActionEvent event) throws IOException {

        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        String error = validatePassword(password);

        if (error != null) {
            showAlert(error);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Passwords do not match");
            return;
        }

        changePass(username, password);

        Parent root = FXMLLoader.load(getClass().getResource("/com/bigo/tindatrack/Login-view.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();

    }

    public void OnBackClick(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/bigo/tindatrack/ForgotPassword-view.fxml"));

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
}
