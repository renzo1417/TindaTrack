package com.bigo.tindatrack.Controller.Inventory;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.data.InventoryList.InventoryList;
import com.bigo.tindatrack.data.StockDetails.StockDetailsList;
import javafx.collections.ObservableList;

import static com.bigo.tindatrack.SQLite_Database.productsManagement.ProductManagement.addProduct;
import static com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable.getInventoryOrderedByStatus;

public class InventoryModel {
    private InventoryList list = new InventoryList();

    public InventoryModel() {
        list = new InventoryList();
        syncWithDatabase();
    }

    public void syncWithDatabase() {
        int ownerId = com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId();
        ObservableList<Product> freshData = getInventoryOrderedByStatus(ownerId);
        list.getProductList().setAll(freshData);
    }

    public boolean saveNewProduct(Product newProduct) {
        int ownerId = com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId();
        String name = newProduct.getProductName();
        int quan = newProduct.getQuantity();
        String expiry = newProduct.getLocalExpiryDate() != null ? newProduct.getLocalExpiryDate().toString() : null;

        int generatedId = com.bigo.tindatrack.SQLite_Database.productsManagement.ProductManagement.addProduct(name, quan, expiry, ownerId);

        if (generatedId != -1) {
            newProduct.setId(generatedId);
            com.bigo.tindatrack.Controller.Notification.NotificationService.onProductAdded(newProduct);
            syncWithDatabase();
            return true;
        }
        return false;
    }

    public boolean removeProduct(Product item) {
        boolean isRemovedFromDB = com.bigo.tindatrack.SQLite_Database.productsManagement.ProductManagement.removeProduct(item.getProductName());
        list.removeProduct(item);

        if(isRemovedFromDB){
            syncWithDatabase();
            return true;
        }

        return false;
    }

    public void modifyProduct(Product item) {
        list.modifyProduct(item);
    }

    public ObservableList<Product> getProductList() {
        return list.getProductList();
    }
}
