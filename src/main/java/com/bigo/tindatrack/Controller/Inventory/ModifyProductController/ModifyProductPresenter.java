package com.bigo.tindatrack.Controller.Inventory.ModifyProductController;

import com.bigo.tindatrack.Product.Product;

import java.time.LocalDate;

public class ModifyProductPresenter {
    private ModifyProductController controller;

    public ModifyProductPresenter(ModifyProductController controller) {
        this.controller = controller;
    }

    public Product modifyProduct(Product product, String productName, String quantity, LocalDate expiryDate, String category) {
        if (productName.trim().isEmpty() || quantity.trim().isEmpty() || category.trim().isEmpty()) {
            controller.showAlert("All fields must be filled except for Expiry Date (Optional)!");
            return null;
        } else if (!quantity.matches("\\d+")) {
            controller.showAlert("Invalid quantity input!");
            return null;
        } else {
            product.setProductName(productName);
            product.setCategory(category);
            product.setExpiryDate(expiryDate);
            product.setOriginalQuantity(Integer.parseInt(quantity));
            product.setQuantity(Integer.parseInt(quantity));
            product.getStatusController().updateStatus(product.getLocalExpiryDate(), product.getQuantity(), product.getOriginalQuantity());
        }

        return product;
    }
}
