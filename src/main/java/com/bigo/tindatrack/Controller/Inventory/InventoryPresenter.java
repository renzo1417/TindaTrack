package com.bigo.tindatrack.Controller.Inventory;

import com.bigo.tindatrack.Controller.Inventory.AddProductController.AddProductController;
import com.bigo.tindatrack.Controller.Inventory.InventoryActionController.ActionController;
import com.bigo.tindatrack.Product.Product;
import javafx.collections.ObservableList;
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

    public void remove(Product item) {
        model.removeProduct(item);
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
                    actionController.getTrashButton().setOnAction(e -> {
                        remove(item);
                    });

                    setGraphic(root);
                }
            }
        };
    }

    public void addNewProduct(Pane addProductPane) {
        addProductPane.getChildren().clear();
        addProductPane.getChildren().add(addProductController.getAddProductPane());
    }
}
