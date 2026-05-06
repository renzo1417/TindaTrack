package com.bigo.tindatrack.Controller.Inventory.ModifyProductController;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.StockManagement.StockActivityManagement;
import com.bigo.tindatrack.data.StockDetails.StockDetails;
import com.bigo.tindatrack.data.StockDetails.StockDetailsList;

public class ModifyProductModel {
    private static StockDetailsList detailsList = new StockDetailsList();

    public void modifiedStockActivity(Product product, int oldQty, int newQty) {
        if (oldQty != newQty) {
            StockDetails newStockDetails = detailsList.modifiedStockActivity(product, oldQty, newQty);
            saveActivityToDB(newStockDetails);
        }
    }

    public boolean saveActivityToDB(StockDetails newStockDetails) {
        int ownerId = com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId();

        String productName = newStockDetails.getProductName();
        int newQty = newStockDetails.getNewQty();
        int oldQty = newStockDetails.getOldQty();
        String reason = newStockDetails.getReason();
        String date = newStockDetails.getDate();

        int generatedID = StockActivityManagement.addActivity(productName, oldQty, newQty, reason, date, ownerId);

        if (generatedID != -1) {
            newStockDetails.setId(generatedID);
            return true;
        }

        return false;
    }
}
