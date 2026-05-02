package com.bigo.tindatrack.data.StockDetails;

import com.bigo.tindatrack.Product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StockDetailsList {
    private static int totalSold = 0;
    private static int totalRestocked = 0;
    private static int totalActivities = 0;
    private static ObservableList<StockDetails> detailsList = FXCollections.observableArrayList();

    private void updateActivity(String reason) {
        if (reason.equals("Restocked")) {
            totalRestocked++;
        } else {
            totalSold++;
        }

        totalActivities = totalSold + totalRestocked;
    }

    public void newStockActivity(Product product) {
        StockDetails newStockDetails = new StockDetails(
                product.getProductName(),
                product.getQuantity(),
                product.getQuantity(),
                "Restocked"
        );

        detailsList.add(0, newStockDetails);
        updateActivity(newStockDetails.getReason());
    }

    public ObservableList<StockDetails> getDetailsList() {
        return detailsList;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public int getTotalRestocked() {
        return totalRestocked;
    }

    public int getTotalActivities() {
        return totalActivities;
    }
}
