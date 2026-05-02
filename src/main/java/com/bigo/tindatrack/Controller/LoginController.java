package com.bigo.tindatrack.Controller;


import static com.bigo.tindatrack.SQLite_Database.userManagement.UserService.getUser;
import com.bigo.tindatrack.utils.utility;
import com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager;
import com.bigo.tindatrack.data.models.User;
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

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private Label forgotPasswordField;

    @FXML
    private Button signInButton;

    @FXML
    private Label createAccountField;

    private void goToDashboard() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/bigo/tindatrack/Dashboard-view.fxml")
            );

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // forgotpassword switch scene
//    @FXML
//    public void goToforgotPassword(MouseEvent event){
//        utility.switchSceneForLabel(event, "/com/bigo/tindatrack/FogotPassword-view.fxml");
//    }


    @FXML
    public void initialize(){
        User user = SessionManager.loadUser();

        if(user!=null){

            javafx.application.Platform.runLater(() -> {
                goToDashboard();
            });

        }

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    public void handleSignInButton(ActionEvent event){
        String username  = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty()){
            showAlert("USERNAME CANNOT BE EMPTY");
        }
        else if(password.isEmpty()){
            showAlert("PASSWORD CANNOT BE EMPTY");
        }
        else{

            User user = getUser(username, password);

            if (user != null) {
                SessionManager.saveUser(user);
                goToDashboard();
            } else {
                showAlert("INCORRECT USERNAME OR PASSWORD");
            }


        }

    }

    @FXML
    public void onCreateAccountClick(MouseEvent event ) throws IOException {
//
        Parent root = FXMLLoader.load(getClass().getResource("/com/bigo/tindatrack/Register-view.fxml"));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();

//        utility.switchSceneForLabel(event, "/com/bigo/tindatrack/Register-view.fxml");
    }


    public void OnForgotPasswordClick(MouseEvent mouseEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/com/bigo/tindatrack/ForgotPassword-view.fxml"));

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
}
