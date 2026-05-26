package com.bigo.tindatrack.Controller.StockActivity;

import com.bigo.tindatrack.data.StockDetails.StockDetails;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.awt.*;

public class StockActivityPresenter {
    private StockActivityController controller;
    private StockActivityModel model;

    public StockActivityPresenter(StockActivityController controller) {
        this.controller = controller;
        model = new StockActivityModel();
    }
    public void updateActivityCount() {
        controller.updateActivityCount(
                model.getTotalRestocked(),
                model.getTotalSold(),
                model.getTotalActivities()
        );
    }

    public void provideFilter(TextField searchTextField, TableView<StockDetails> detailsTableView) {
        FilteredList<StockDetails> filteredList = new FilteredList<>(model.getList(), p -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(action -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return action.getProductName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<StockDetails> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(detailsTableView.comparatorProperty());

        detailsTableView.setItems(sortedList);
    }

    public ObservableList<StockDetails> getList() {
        return model.getList();
    }
}
