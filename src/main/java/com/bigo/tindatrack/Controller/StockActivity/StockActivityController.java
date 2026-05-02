package com.bigo.tindatrack.Controller.StockActivity;

import com.bigo.tindatrack.data.StockDetails.StockDetails;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableColumn<StockDetails, StockDetails> reasonColumn;
    @FXML
    private TableColumn<StockDetails, String> dateColumn;

    private StockActivityPresenter presenter;

    @FXML
    public void initialize() {
        presenter = new StockActivityPresenter(this);

        detailsTableView.setItems(presenter.getList());
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        oldQtyColumn.setCellValueFactory(new PropertyValueFactory<>("oldQty"));
        newQtyColumn.setCellValueFactory(new PropertyValueFactory<>("newQty"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        presenter.setupListener();
    }

    public void updateActivityCount(int totalRestocked, int totalSold, int totalActivities) {
        totalRestockLabel.setText(totalRestocked + "");
        totalLabel.setText(totalActivities + "");
        totalSoldLabel.setText(totalSold + "");
    }
}
