package com.bigo.tindatrack.Controller;

import com.bigo.tindatrack.utils.utility;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.Objects;


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



    public void handleVerifyAndContinue(ActionEvent event){

    }

    @FXML
    public void forgotPasswordgoToDashboard(Event event){
        utility.switchSceneForLabel(event, "/com/bigo/tindatrack/Login-view.fxml");
    }

}
