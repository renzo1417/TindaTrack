package com.bigo.tindatrack.data.InventoryList;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.data.StockDetails.StockDetails;
import com.bigo.tindatrack.data.StockDetails.StockDetailsList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static com.bigo.tindatrack.SQLite_Database.productsManagement.fetchDataFromTable.getInventoryOrderedByStatus;


public class InventoryList {
    private static StockDetailsList detailsList = new StockDetailsList();
    private static ObservableList<Product> productList = FXCollections.observableArrayList();

    public StockDetails addNewProduct(Product newProduct) {
        productList.add(0, newProduct);
        System.out.println("UPDATE LIST ID: " + System.identityHashCode(detailsList.getDetailsList()));
        return detailsList.newStockActivity(newProduct);
    }

    public void removeProduct(Product item) {
        productList.remove(item);
    }

    public StockDetails replenishedProduct(Product item, int oldQty, int newQty) {
        return detailsList.modifiedStockActivity(item, oldQty, newQty);
    }

    public void modifyProduct(Product item) {
        productList.remove(item);
        productList.add(0, item);
    }

    public ObservableList<Product> getProductList() {
        return productList;
    }

    public void loadItems() {
        int ownerId = com.bigo.tindatrack.SQLite_Database.userManagement.SessionManager.getCurrentUserId();
        ObservableList<Product> freshData = getInventoryOrderedByStatus(ownerId);
        productList.setAll(freshData);
    }
}
