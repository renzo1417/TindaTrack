package com.bigo.tindatrack.data.StockDetails;

import java.time.LocalDate;

public class StockDetails {
    private String productName;
    private int oldQty;
    private int newQty;
    private String reason;
    private LocalDate date;

    public StockDetails(String productName, int oldQty, int newQty, String reason) {
        this.productName = productName;
        this.oldQty = oldQty;
        this.newQty = newQty;
        this.reason = reason;
        date = LocalDate.now();
    }

    public String getReason() {
        return reason;
    }

    public String getProductName() {
        return productName;
    }

    public int getOldQty() {
        return oldQty;
    }

    public int getNewQty() {
        return newQty;
    }

    public String getDate() {
        return date.toString();
    }
}
