package com.bigo.tindatrack.Controller.Inventory.ModifyProductController;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.data.StockDetails.StockDetailsList;

public class ModifyProductModel {
    private static StockDetailsList detailsList = new StockDetailsList();

    public void modifiedStockActivity(Product product, int oldQty, int newQty) {
        if (oldQty != newQty) {
            detailsList.modifiedStockActivity(product, oldQty, newQty);
        }
    }
}
