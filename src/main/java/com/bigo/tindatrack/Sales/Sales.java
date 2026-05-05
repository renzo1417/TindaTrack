package com.bigo.tindatrack.Sales;

public class Sales {

    private int id;
    private int productId;
    private String name;
    private int quantity;
    private String saleDate;

    public Sales(int id, int productId, String name, int quantity, String saleDate) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.saleDate = saleDate;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSaleDate() {
        return saleDate;
    }

}
