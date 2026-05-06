package com.bigo.tindatrack.Controller.StockActivity;

import com.bigo.tindatrack.data.StockDetails.StockDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class StockActivityController {
    @FXML
    private Label totalRestockLabel;
    @FXML
    private Label totalSoldLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private TableView<StockDetails> detailsTableView;
    @FXML
    private TableColumn<StockDetails, String> productNameColumn;
    @FXML
    private TableColumn<StockDetails, String> oldQtyColumn;
    @FXML
    private TableColumn<StockDetails, String> newQtyColumn;
    @FXML
    private TableColumn<StockDetails, Pane> changeColumn;
    @FXML
    private TableColumn<StockDetails, Pane> reasonColumn;
    @FXML
    private TableColumn<StockDetails, String> dateColumn;
    @FXML
    private TextField searchTextField;

    private StockActivityPresenter presenter;

    @FXML
    public void initialize() {
        presenter = new StockActivityPresenter(this);

        detailsTableView.setItems(presenter.getList());
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        oldQtyColumn.setCellValueFactory(new PropertyValueFactory<>("oldQty"));
        newQtyColumn.setCellValueFactory(new PropertyValueFactory<>("newQty"));
        changeColumn.setCellValueFactory(new PropertyValueFactory<>("change"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reasoning"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        presenter.updateActivityCount();

//        presenter.setupListener();
        presenter.provideFilter(searchTextField, detailsTableView);
    }

    public void updateActivityCount(int totalRestocked, int totalSold, int totalActivities) {
        System.out.println(totalRestocked);
        System.out.println(totalSold);
        System.out.println(totalActivities);
        totalRestockLabel.setText(totalRestocked + "");
        totalLabel.setText(totalActivities + "");
        totalSoldLabel.setText(totalSold + "");
    }

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

    @FXML
    public void switchToInventory(ActionEvent event) {
        switchScene(event,"/com/bigo/tindatrack/Inventory-view.fxml");
    }
}
