package com.bigo.tindatrack.Controller.Inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class InventoryController {
    @FXML
    private Button addProductBtn;
    @FXML
    private Pane unclickablePane;
    @FXML
    private Pane addProductPane;

    private InventoryPresenter presenter;

    @FXML
    public void initialize() {
        presenter = new InventoryPresenter(this);
    }

    @FXML
    public void addNewProduct() {
        unclickablePane.setVisible(true);
        presenter.addNewProduct(addProductPane);
        addProductPane.setVisible(true);
    }




    //this is helper function for switching screens
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

    // these are function that switches screen from inventory more to add.
    @FXML
    public void setToDashboard(ActionEvent event){
        switchScene(event,"/com/bigo/tindatrack/Dashboard-view.fxml");
    }

    //logout implementation
    public void setInventoryLogout(ActionEvent event){
        com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.clearSession();
        switchScene(event, "/com/bigo/tindatrack/Login-view.fxml");
    }
    protected void hideAddPopOut() {
        if (unclickablePane.isVisible()) {
            unclickablePane.setVisible(false);
        }

        if (addProductPane.isVisible()) {
            addProductPane.setVisible(false);
        }
    }
}
