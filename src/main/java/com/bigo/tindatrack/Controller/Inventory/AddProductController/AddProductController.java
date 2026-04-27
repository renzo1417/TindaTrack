package com.bigo.tindatrack.Controller.Inventory.AddProductController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

public class AddProductController {
    @FXML
    private Pane addProductPane;
    @FXML
    private Button exitButton;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private Button cancelButton;

    public Pane getAddProductPane() {
        return addProductPane;
    }

    public Button getExitButton() {
        return exitButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    @FXML
    public void initialize() {
        categoryComboBox.getItems().addAll(
                "Beverages", "Dairy",
                "Produce", "Snacks",
                "Pantry Staples", "Grains",
                "Frozen", "Canned Goods",
                "Condiments", "Personal Care", "Cleaning"
        );
    }
}
