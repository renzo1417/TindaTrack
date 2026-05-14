package com.bigo.tindatrack.Controller.Inventory;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.SQLite_Database.StockManagement.StockActivityManagement;
import com.bigo.tindatrack.data.InventoryList.InventoryList;
import com.bigo.tindatrack.data.StockDetails.StockDetails;
import com.bigo.tindatrack.data.StockDetails.StockDetailsList;
import javafx.collections.ObservableList;

import static com.bigo.tindatrack.SQLite_Database.productsManagement.ProductManagement.addProduct;
import static com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable.getInventoryOrderedByStatus;

public class InventoryModel {
    private InventoryList list = new InventoryList();

    public InventoryModel() {
        syncWithDatabase();
    }

    public void syncWithDatabase() {
        int ownerId = com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId();
        ObservableList<Product> freshData = getInventoryOrderedByStatus(ownerId);
        list.getProductList().setAll(freshData);
    }

    public boolean saveNewProduct(Product newProduct) {
        StockDetails newStockDetails = list.addNewProduct(newProduct);
        saveActivityToDB(newStockDetails);
        return saveProductToDB(newProduct);
    }

    private boolean saveActivityToDB(StockDetails newStockDetails) {
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

    private boolean saveProductToDB(Product newProduct) {
        int ownerId = com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId();
        String name = newProduct.getProductName();
        int quan = newProduct.getQuantity();
        String expiry = newProduct.getLocalExpiryDate() != null ? newProduct.getLocalExpiryDate().toString() : null;
        String category = newProduct.getCategory();
        int originalQty = newProduct.getOriginalQuantity();
        int generatedId = com.bigo.tindatrack.SQLite_Database.productsManagement.ProductManagement.addProduct(name, quan, expiry, ownerId, category, originalQty);

        if (generatedId != -1) {
            newProduct.setId(generatedId);
            com.bigo.tindatrack.Controller.Notification.NotificationService.onProductAdded(newProduct);
            return true;
        }
        return false;
    }

    public boolean removeProduct(Product item) {
        list.removeProduct(item);

        com.bigo.tindatrack.Controller.Notification.NotificationService
                .onProductDeleted(item.getId());

        return removeProductFromDB(item);
    }

    public boolean removeProductFromDB(Product item) {
        boolean isRemovedFromDB = com.bigo.tindatrack.SQLite_Database.productsManagement.ProductManagement.removeProduct(item.getProductName());

        if(isRemovedFromDB){
            return true;
        }

        return false;
    }

    public boolean modifyProduct(Product item) {
        list.modifyProduct(item);

        return modifyProductInDB(item);
    }

    public boolean modifyProductInDB(Product item) {
        boolean isModifiedFromDB = com.bigo.tindatrack.SQLite_Database.productsManagement.ProductManagement.modifyProduct(item);

        if (isModifiedFromDB) {
            return true;
        }

        return false;
    }

    public ObservableList<Product> getProductList() {
        return list.getProductList();
    }
}
