package com.bigo.tindatrack.data.InventoryList;

import com.bigo.tindatrack.Product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class InventoryList {
    private static ObservableList<Product> productList = FXCollections.observableArrayList();

    public void addNewProduct(Product newProduct) {
        productList.add(0, newProduct);
    }

    public void removeProduct(Product item) {
        productList.remove(item);
    }

    public void modifyProduct(Product item) {
        productList.remove(item);
        productList.add(0, item);
    }

    public ObservableList<Product> getProductList() {
        return productList;
    }
}
