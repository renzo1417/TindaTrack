package com.bigo.tindatrack.Controller.Inventory;

import com.bigo.tindatrack.Product.Product;
import com.bigo.tindatrack.data.InventoryList.InventoryList;

public class InventoryModel {
    public void saveNewProduct(Product newProduct) {
        InventoryList.addNewProduct(newProduct);
    }
}
