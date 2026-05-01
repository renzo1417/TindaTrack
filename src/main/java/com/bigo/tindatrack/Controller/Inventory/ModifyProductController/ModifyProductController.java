package com.bigo.tindatrack.Controller.Inventory.ModifyProductController;

import com.bigo.tindatrack.Controller.Inventory.AddProductController.AddProductPresenter;
import com.bigo.tindatrack.Product.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.time.LocalDate;

public class ModifyProductController {
    @FXML
    private Pane ModifyProductPane;
    @FXML
    private Button exitButton;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveProductButton;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField quantityTextFIeld;
    @FXML
    private DatePicker expiryDatePicker;
    @FXML
    private Button resetButton;

    private Product toBemodified = null;

    private ModifyProductPresenter presenter;

    public Pane getModifyProductPane() { return ModifyProductPane; }

    public Button getExitButton() {
        return exitButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getSaveProductButton() { return saveProductButton; }

    @FXML
    public void initialize() {
        categoryComboBox.getItems().addAll(
                "Beverages", "Dairy",
                "Produce", "Snacks",
                "Pantry Staples", "Grains",
                "Frozen", "Canned Goods",
                "Condiments", "Personal Care", "Cleaning"
        );

        presenter = new ModifyProductPresenter(this);
    }

    @FXML
    public void resetFields() {
        productNameTextField.setText(toBemodified.getProductName());
        quantityTextFIeld.setText(toBemodified.getQuantity() + "");
        expiryDatePicker.setValue(toBemodified.getLocalExpiryDate());
        categoryComboBox.setValue(toBemodified.getCategory());
    }

    public void clearInputs() {
        productNameTextField.clear();
        quantityTextFIeld.clear();
        expiryDatePicker.setValue(null);
        categoryComboBox.setValue(null);
    }

    public void loadProduct(Product product) {
        String productName = product.getProductName();
        LocalDate expiryDate = product.getLocalExpiryDate();
        String category = product.getCategory();
        int quantity = product.getQuantity();

        productNameTextField.setText(productName);
        quantityTextFIeld.setText(quantity + "");
        expiryDatePicker.setValue(expiryDate);
        categoryComboBox.setValue(category);

        toBemodified = product;
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Product saveModifiedProduct() {
        String productName = productNameTextField.getText();
        LocalDate expiryDate = expiryDatePicker.getValue();
        String category = categoryComboBox.getValue();
        String quantity = quantityTextFIeld.getText();

        return presenter.modifyProduct(toBemodified, productName, quantity, expiryDate, category);
    }

}
