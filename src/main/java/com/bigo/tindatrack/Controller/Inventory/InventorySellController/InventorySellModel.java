package com.bigo.tindatrack.Controller.Inventory.InventorySellController;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.SalesManagement.SalesManagement;
import com.bigo.tindatrack.SQLite_Database.StockManagement.StockActivityManagement;
import com.bigo.tindatrack.data.InventoryList.InventoryList;

import java.time.LocalDate;

public class InventorySellModel {
    private InventoryList list = new InventoryList();

    public void modifyProduct(Product item, int sold) {
        list.modifyProduct(item);
        saveModifiedProductToDB(item, sold);
    }

    public void saveModifiedProductToDB(Product item, int quantitySold) {
        int owner_id = com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId();
        SalesManagement.recordSale(owner_id, item.getId(), item.getProductName(), quantitySold);
        StockActivityManagement.addActivity(item.getProductName(),
                item.getQuantity() + quantitySold,
                item.getQuantity(),
                "Sold",LocalDate.now().toString()
                , owner_id);
    }
}
