package com.bigo.tindatrack.Controller.StockActivity;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.StockManagement.StockFetchFromTable;
import com.bigo.tindatrack.data.StockDetails.StockDetails;
import com.bigo.tindatrack.data.StockDetails.StockDetailsList;
import javafx.collections.ObservableList;

import static com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable.getInventoryOrderedByStatus;

public class StockActivityModel {
    private StockDetailsList list = new StockDetailsList();

    public StockActivityModel() {
        syncWithDatabase();
    }

    private void syncWithDatabase() {
        int ownerId = com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId();
        ObservableList<StockDetails> freshData = StockFetchFromTable.getActivitiesFromDB(ownerId);
        list.getDetailsList().setAll(freshData);
        list.resychActivities();
    }

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
