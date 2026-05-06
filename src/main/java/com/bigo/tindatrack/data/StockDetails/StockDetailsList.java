package com.bigo.tindatrack.data.StockDetails;

import com.bigo.tindatrack.Product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StockDetailsList {
    private int totalSold = 0;
    private int totalRestocked = 0;
    private int totalActivities = 0;
    private static ObservableList<StockDetails> detailsList = FXCollections.observableArrayList();

    private void updateActivity(String reason) {
        if (reason.equals("Restocked")) {
            totalRestocked++;
        } else {
            totalSold++;
        }

        totalActivities = totalSold + totalRestocked;
    }

    public StockDetails newStockActivity(Product product) {
        StockDetails newStockDetails = new StockDetails(
                product.getProductName(),
                product.getQuantity(),
                product.getQuantity(),
                "Restocked"
        );

        detailsList.add(0, newStockDetails);
        updateActivity(newStockDetails.getReason());

        return newStockDetails;
    }

    public StockDetails modifiedStockActivity(Product product, int oldQty, int newQty) {
        String reason;

        if (oldQty > newQty) {
            reason = "Sold";
        } else {
            reason = "Restocked";
        }

        StockDetails newStockDetails = new StockDetails(
            product.getProductName(),
            oldQty,
            newQty,
            reason
        );

        detailsList.add(0, newStockDetails);
        updateActivity(newStockDetails.getReason());

        return newStockDetails;
    }

    public ObservableList<StockDetails> getDetailsList() {
        return detailsList;
    }

    public void resychActivities() {
        this.totalSold = 0;
        this.totalRestocked = 0;
        this.totalActivities = 0;

        for (StockDetails s : detailsList) {
            updateActivity(s.getReason());
        }
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
