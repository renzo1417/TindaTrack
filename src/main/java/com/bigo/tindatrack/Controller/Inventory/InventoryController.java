package com.bigo.tindatrack.Controller.Inventory;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.userManagement.UserUIHelper;
import com.bigo.tindatrack.data.models.User;
import com.bigo.tindatrack.utils.utility;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.scene.layout.Pane;

import java.io.IOException;

import static com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable.getInventoryOrderedByStatus;
import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId;
import static com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.loadUser;

public class InventoryController {
    @FXML
    private Button addProductPopoutBtn;
    @FXML
    private Pane unclickablePane;
    @FXML
    private Pane addProductPane;
    @FXML
    private TableView<Product> inventoryTableView;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, String> expiryDateColumn;
    @FXML
    private TableColumn<Product, Pane> statusColumn;
    @FXML
    private TableColumn<Product, Product> actionColumn;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<String> statusFilter;
    @FXML
    private Label username_top, username_bottom, username_top_initial, username_bottom_initial;

    private InventoryPresenter presenter;

    private User user = loadUser();

    @FXML
    public void initialize() {
        if (user == null) {
            System.out.println("Error: No user found!");
            return;
        }

//        username_top.setText(user.getUsername());
//        username_bottom.setText(user.getUsername());
        UserUIHelper.setupUserUI(username_top_initial,
                username_bottom_initial,
                username_top,
                username_bottom,
                loadUser());


        presenter = new InventoryPresenter(this);

       inventoryTableView.setItems(presenter.getProductList());
       productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
       categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
       quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
       expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
       statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
       actionColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue()));
       actionColumn.setCellFactory(column -> presenter.buildActionCell());

       statusFilter.getItems().addAll(
               "All Status",
               "Empty",
               "Expired",
               "Safe",
               "Near Expiry",
               "Low Stock"
       );

       statusFilter.setValue("All Status");

       inventoryTableView.setSelectionModel(null);
       presenter.setupMasterFilter(searchTextField, statusFilter, inventoryTableView);
    }

    @FXML
    public void addNewProductPopout() {
        unclickablePane.setVisible(true);
        presenter.addNewProduct(addProductPane);
        addProductPane.setVisible(true);
    }

    public void modifyProductPopout() {
        unclickablePane.setVisible(true);
        presenter.showModifyProductPopout(addProductPane);
        addProductPane.setVisible(true);
    }

    public void sellProductPopout() {
        unclickablePane.setVisible(true);
        presenter.showSellPopout(addProductPane);
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
    public void goToInventory(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Inventory-view.fxml");
    }

    public void goToInsightButton(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Insights-view.fxml");
    }

    public void goToStockActivity(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/StockActivity-view.fxml");
    }

    public void goToSetting(ActionEvent event) {
        System.out.println("setting button");
        utility.switchScene(event, "/com/bigo/tindatrack/SettingsMarket-view.fxml");
    }

    public void goToDashboard(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Dashboard-view.fxml");
    }

    public void goToNotifications(ActionEvent event) {
        utility.switchScene(event, "/com/bigo/tindatrack/Notification-view.fxml");
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

    public void refreshTable() {
        inventoryTableView.refresh();
    }
}
