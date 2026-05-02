package com.bigo.tindatrack.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static com.bigo.tindatrack.SQLite_Database.userManagement.PasswordHandler.verifyUser;


public class ForgotPasswordController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField storeNameField;

    @FXML
    private Button verifyButton;

    @FXML
    private Label signUpLabel;

    @FXML
    private  Label backToSignInLink;

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void handleVerifyAndContinue(ActionEvent event) throws IOException {

        String fullName = fullNameField.getText();
        String phone = phoneField.getText();
        String storeName = storeNameField.getText();

        String username_result = verifyUser(fullName, phone, storeName);

        if (fullName.isEmpty()) {
            showAlert("Please enter full name");
        }
        else if (phone.isEmpty()) {
            showAlert("Please enter phone number");
        }
        else if (storeName.isEmpty()) {
            showAlert("Please enter store name");
        }else{

            if(username_result == null){
                showAlert("Account does not exist");
            }else{

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/bigo/tindatrack/ForgotPasswordtwo-view.fxml"));


                Parent root = loader.load();

                ForgotPasswordVerificationTwoController controller = loader.getController();
                controller.setUsername(username_result);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();


            }

        }



    }


    public void OnBackToSignInClick(MouseEvent mouseEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/com/bigo/tindatrack/Login-view.fxml"));

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void OnSignInClick(MouseEvent mouseEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/com/bigo/tindatrack/Login-view.fxml"));

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
}
