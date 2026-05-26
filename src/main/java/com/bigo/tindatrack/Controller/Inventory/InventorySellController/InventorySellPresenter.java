package com.bigo.tindatrack.Controller.Inventory.InventorySellController;

import com.bigo.tindatrack.Product.Product;

public class InventorySellPresenter {
    InventorySellController controller;
    InventorySellModel model;

    public InventorySellPresenter(InventorySellController controller) {
        this.controller = controller;
        model = new InventorySellModel();
    }

    public boolean confirmSales(String quantity, Product item) {
        if (quantity.isEmpty()) {
            controller.showAlert("Invalid quantity: SHOULD NOT BE EMPTY");
        } else if (!quantity.matches("\\d+")) {
            controller.showAlert("Invalid quantity: SHOULD BE NUMBERS ONLY");
        } else {
            int sold = Integer.parseInt(quantity);

            int newQty = item.getQuantity() - sold;
            if (newQty < 0) {
                newQty = 0;
                sold = item.getQuantity();
            }
            item.setQuantity(newQty);

            item.getStatusController().updateStatus(item.getLocalExpiryDate(), item.getQuantity(), item.getOriginalQuantity());
            model.modifyProduct(item, sold);
            return true;
        }

        return false;
    }
}
