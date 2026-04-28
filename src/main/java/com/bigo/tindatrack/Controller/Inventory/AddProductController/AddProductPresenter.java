package com.bigo.tindatrack.Controller.Inventory.AddProductController;

import com.bigo.tindatrack.Product.Product;

import java.time.LocalDate;

public class AddProductPresenter {
    private AddProductController controller;

    public AddProductPresenter(AddProductController controller) {
        this.controller = controller;
    }

    public Product createProduct(String productName, String quantity, LocalDate expiryDate, String category) {
        Product newProduct = null;
        if (productName.trim().isEmpty() || quantity.trim().isEmpty() || category.trim().isEmpty()) {
            controller.showAlert("All fields must be filled except for Expiry Date (Optional)!");
        } else if (!quantity.matches("\\d+")) {
            controller.showAlert("Invalid quantity input!");
        } else {
            newProduct = new Product(productName, Integer.parseInt(quantity), expiryDate, category);
        }

        return newProduct;
    }
}
