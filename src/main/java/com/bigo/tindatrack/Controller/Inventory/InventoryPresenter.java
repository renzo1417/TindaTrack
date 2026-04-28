package com.bigo.tindatrack.Controller.Inventory;

import com.bigo.tindatrack.Controller.Inventory.AddProductController.AddProductController;
import com.bigo.tindatrack.Product.Product;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class InventoryPresenter {
    private InventoryController controller;
    private InventoryModel model;
    private AddProductController addProductController;

    public InventoryPresenter(InventoryController controller) {
        this.controller = controller;
        model = new InventoryModel();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bigo/tindatrack/AddProduct-view.fxml"));
            Parent root = loader.load();

            addProductController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        addProductController.getExitButton().setOnAction(event -> {
            controller.hideAddPopOut();
            addProductController.clearInputs();
        });

        addProductController.getCancelButton().setOnAction(event -> {
            controller.hideAddPopOut();
            addProductController.clearInputs();
        });

        addProductController.getAddProductButton().setOnAction(event -> {
            Product newProduct = addProductController.addNewProduct();

            if (newProduct != null) {
                model.saveNewProduct(newProduct);
                controller.hideAddPopOut();
                addProductController.clearInputs();
            }
        });
    }

    public void addNewProduct(Pane addProductPane) {
        addProductPane.getChildren().clear();
        addProductPane.getChildren().add(addProductController.getAddProductPane());
    }
}
