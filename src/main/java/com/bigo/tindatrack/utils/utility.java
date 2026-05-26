package com.bigo.tindatrack.utils;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class utility {

    //helper for the switch screens
    public static void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(utility.class.getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Navigation Error: Unable to load " + fxmlPath);
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("File Error: FXML path not found: " + fxmlPath);
        }
    }

    public static void switchToNotification(Event event){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(utility.class.getResource("/com/bigo/tindatrack/Notification-view.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Navigation Error: Unable to load notification view");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("File Error: FXML path not found: notification view");
        }
    }

    // helper for label forgotpassword
    public static void switchSceneForLabel(Event event, String fxmlPath) {
        try {
            Parent root =FXMLLoader.load(Objects.requireNonNull(utility.class.getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Navigation Error: Unable to load " + fxmlPath);

            e.printStackTrace();
        }
    }

}