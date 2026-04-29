package com.bigo.tindatrack.Controller.Inventory;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.data.InventoryList.InventoryList;
import javafx.collections.ObservableList;

public class InventoryModel {
    private InventoryList list = new InventoryList();

    public void saveNewProduct(Product newProduct) {
        list.addNewProduct(newProduct);
    }

    public void removeProduct(Product item) {
        list.removeProduct(item);
    }

    public ObservableList<Product> getProductList() {
        return list.getProductList();
    }
}
