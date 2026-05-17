package com.bigo.tindatrack.Controller.Inventory.InventorySellController;

import com.bigo.tindatrack.Product.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class InventorySellController {
    @FXML
    private Pane sellProductPane;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productCategoryLabel;
    @FXML
    private Label stockDetailsLabel;
    @FXML
    private Button closeButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButtom;
    @FXML
    private TextField inputSoldTextField;

    private InventorySellPresenter presenter;

    private Product toBeSold;

    @FXML
    public void initialize() {
        presenter = new InventorySellPresenter(this);
    }

    public Pane getSellProductPane() {
        return sellProductPane;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getConfirmButtom() {
        return confirmButtom;
    }

    public boolean  confirmSales() {
        return presenter.confirmSales(inputSoldTextField.getText(), toBeSold);
    }

    public void loadProductToSell(Product item, int totalSales) {
        toBeSold = item;

        String name = item.getProductName();
        int availableStock = item.getQuantity();
        String category = item.getCategory();

        productNameLabel.setText(name);
        productCategoryLabel.setText(category);
        stockDetailsLabel.setText("Available stock: " + availableStock + " units · Total sold: " + totalSales + " units");
    }

    public void restartFields() {
        productNameLabel.setText("Product Name");
        productCategoryLabel.setText("Category");
        stockDetailsLabel.setText("Available stock: --- units · Total sold: --- units");
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
