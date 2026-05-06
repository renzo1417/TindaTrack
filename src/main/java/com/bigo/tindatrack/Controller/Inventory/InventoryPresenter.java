package com.bigo.tindatrack.Controller.Inventory;

import com.bigo.tindatrack.Controller.Inventory.AddProductController.AddProductController;
import com.bigo.tindatrack.Controller.Inventory.InventoryActionController.ActionController;
import com.bigo.tindatrack.Controller.Inventory.ModifyProductController.ModifyProductController;
import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.SalesManagement.SalesManagement;
import com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager;
import com.bigo.tindatrack.SQLite_Database.userManagement.UserService;
import com.bigo.tindatrack.data.models.User;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class InventoryPresenter {
    private InventoryController controller;
    private InventoryModel model;
    private AddProductController addProductController;
    private ModifyProductController modifyProductController;

    private FilteredList<Product> filteredList;

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
                // model handles all work (notif, owner id and database)
                boolean success = model.saveNewProduct(newProduct);

                if (success) {
                    controller.hideAddPopOut();
                    addProductController.clearInputs();
                } else {
                    System.err.println("Failed to save to database!");
                }
            }
        });

        modifyProductController.getSaveProductButton().setOnAction(event -> {
            Product modifiedProduct = modifyProductController.saveModifiedProduct();

            if (modifiedProduct != null) {

                int oldQuantity = modifyProductController.getOld_quantity();
                int newQuantity = modifiedProduct.getQuantity();

                if(newQuantity < oldQuantity){
                    int sold =  oldQuantity - newQuantity;

                    SalesManagement.recordSale(SessionManager.loadUser().getID(), modifiedProduct.getId(), modifiedProduct.getProductName(), sold);

                    System.out.println("Sold: " + sold + " Product ID: " + modifiedProduct.getId() + " Owner ID: " + SessionManager.loadUser().getID());
                }


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
                    actionController.getTrashButton().setOnAction(e -> {
                        remove(item);
                    });
                    actionController.getModifyButton().setOnAction(e -> {
                        modify(item);
                    });

                    setGraphic(root);
                }
            }
        };
    }

    public void setupMasterFilter(TextField searchTextField, ComboBox<String> statusFilter, TableView<Product> inventoryTableView) {
        filteredList = new FilteredList<>(model.getProductList(), p -> true);

        Runnable applyFilters = () -> {
            String searchText = searchTextField.getText() == null ? "" : searchTextField.getText().toLowerCase();
            String selectedStatus = statusFilter.getValue();

            filteredList.setPredicate(product -> {
                boolean matchesSearch = searchText.isEmpty() ||
                        product.getProductName().toLowerCase().contains(searchText);

                boolean matchesStatus = (selectedStatus == null || selectedStatus.equals("All Status")) ||
                        product.getStatusString().equals(selectedStatus);

                return matchesSearch && matchesStatus;
            });
        };

        searchTextField.textProperty().addListener((obs, old, newVal) -> applyFilters.run());
        statusFilter.valueProperty().addListener((obs, old, newVal) -> applyFilters.run());

        SortedList<Product> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(inventoryTableView.comparatorProperty());

        inventoryTableView.setItems(sortedData);
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
