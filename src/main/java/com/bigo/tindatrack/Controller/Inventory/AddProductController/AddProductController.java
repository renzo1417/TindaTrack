package com.bigo.tindatrack.Controller.Inventory.AddProductController;

import com.bigo.tindatrack.Product.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.time.LocalDate;

public class AddProductController {
    @FXML
    private Pane addProductPane;
    @FXML
    private Button exitButton;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Button addProductButton;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField quantityTextFIeld;
    @FXML
    private DatePicker expiryDatePicker;

    private AddProductPresenter presenter;

    public Pane getAddProductPane() {
        return addProductPane;
    }

    public Button getExitButton() {
        return exitButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getAddProductButton() { return addProductButton; }

    @FXML
    public void initialize() {
        categoryComboBox.getItems().addAll(
                "Beverages", "Dairy",
                "Produce", "Snacks",
                "Pantry Staples", "Grains",
                "Frozen", "Canned Goods",
                "Condiments", "Personal Care", "Cleaning"
        );

        presenter = new AddProductPresenter(this);
    }

    public Product addNewProduct() {
        String productName = productNameTextField.getText();
        String quantity = quantityTextFIeld.getText();
        LocalDate expiryDate = expiryDatePicker.getValue();
        String category = categoryComboBox.getValue();

        return presenter.createProduct(productName, quantity, expiryDate, category);
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void clearInputs() {
        productNameTextField.clear();
        quantityTextFIeld.clear();
        expiryDatePicker.setValue(null);
        categoryComboBox.setValue(null);
    }
}
