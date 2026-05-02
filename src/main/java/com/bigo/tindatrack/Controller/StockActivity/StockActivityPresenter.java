package com.bigo.tindatrack.Controller.StockActivity;

import com.bigo.tindatrack.data.StockDetails.StockDetails;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class StockActivityPresenter {
    private StockActivityController controller;
    private StockActivityModel model;

    public StockActivityPresenter(StockActivityController controller) {
        this.controller = controller;
        model = new StockActivityModel();
    }

    public void setupListener() {
        model.getList().addListener((ListChangeListener<StockDetails>) change -> {
            while (change.next()) {
                controller.updateActivityCount(
                        model.getTotalRestocked(),
                        model.getTotalSold(),
                        model.getTotalActivities()
                );
                System.out.println("TEEEEEEESTTTT!!!!!!");
            }
        });
    }

    public ObservableList<StockDetails> getList() {
        return model.getList();
    }
}
