package com.bigo.tindatrack.Controller.Inventory;

import com.bigo.tindatrack.Controller.Inventory.AddProductController.AddProductController;
import com.bigo.tindatrack.Controller.Inventory.InventoryActionController.ActionController;
import com.bigo.tindatrack.Controller.Inventory.ModifyProductController.ModifyProductController;
import com.bigo.tindatrack.Product.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class InventoryPresenter {
    private InventoryController controller;
    private InventoryModel model;
    private AddProductController addProductController;
    private ModifyProductController modifyProductController;

    public InventoryPresenter(InventoryController controller) {
        this.controller = controller;
        model = new InventoryModel();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bigo/tindatrack/AddProduct-view.fxml"));
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/com/bigo/tindatrack/ModifyProduct-view.fxml"));

            Parent root = loader.load();
            Parent root2 = loader2.load();

            addProductController = loader.getController();
            modifyProductController = loader2.getController();
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

        modifyProductController.getCancelButton().setOnAction(actionEvent -> {
            controller.hideAddPopOut();
            modifyProductController.clearInputs();
        });

        modifyProductController.getExitButton().setOnAction(actionEvent -> {
            controller.hideAddPopOut();
            modifyProductController.clearInputs();
        });

        addProductController.getAddProductButton().setOnAction(event -> {
            Product newProduct = addProductController.addNewProduct();

            if (newProduct != null) {
                model.saveNewProduct(newProduct);
                controller.hideAddPopOut();
                addProductController.clearInputs();
            }
        });

        modifyProductController.getSaveProductButton().setOnAction(event -> {
            Product modifiedProduct = modifyProductController.saveModifiedProduct();

            if (modifiedProduct != null) {
                model.modifyProduct(modifiedProduct);
                controller.hideAddPopOut();
                modifyProductController.clearInputs();
                controller.refreshTable();
            }
        });
    }

    public void remove(Product item) {
        model.removeProduct(item);
    }

    public void modify(Product item) {
        controller.modifyProductPopout();
        modifyProductController.loadProduct(item);
    }

    public ObservableList<Product> getProductList() {
        return model.getProductList();
    }

    public TableCell<Product, Product> buildActionCell() {
        return new TableCell<Product, Product>() {
            private Parent root;
            private ActionController actionController;

            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bigo/tindatrack/InventoryAction-view.fxml"));
                    root = loader.load();
                    actionController = loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    actionController.getTrashButton().setOnAction(e -> { remove(item); });
                    actionController.getModifyButton().setOnAction(e -> { modify(item); });

                    setGraphic(root);
                }
            }
        };
    }

    public void addNewProduct(Pane addProductPane) {
        addProductPane.getChildren().clear();
        addProductPane.getChildren().add(addProductController.getAddProductPane());
    }

    public void showModifyProductPopout(Pane addProductPane) {
        addProductPane.getChildren().clear();
        addProductPane.getChildren().add(modifyProductController.getModifyProductPane());
    }
}
