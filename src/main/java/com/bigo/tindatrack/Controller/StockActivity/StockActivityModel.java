package com.bigo.tindatrack.Controller.StockActivity;

import com.bigo.tindatrack.data.StockDetails.StockDetails;
import com.bigo.tindatrack.data.StockDetails.StockDetailsList;
import javafx.collections.ObservableList;

public class StockActivityModel {
    private StockDetailsList list = new StockDetailsList();

    public ObservableList<StockDetails> getList() {
        return list.getDetailsList();
    }

    public int getTotalSold() {
        return list.getTotalSold();
    }

    public int getTotalRestocked() {
        return list.getTotalRestocked();
    }

    public int getTotalActivities() {
        return list.getTotalActivities();
    }
}
