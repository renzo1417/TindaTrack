package com.bigo.tindatrack.data.InventoryList;

import com.bigo.tindatrack.Product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class InventoryList {
    private static ObservableList<Product> productList = FXCollections.observableArrayList();

    public static void addNewProduct(Product newProduct) {
        productList.add(0, newProduct);
    }

    public static ObservableList<Product> getProductList() {
        return productList;
    }
}
