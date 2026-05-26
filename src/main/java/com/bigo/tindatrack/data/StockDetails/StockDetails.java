package com.bigo.tindatrack.data.StockDetails;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.time.LocalDate;

public class StockDetails {
    private int id;
    private String productName;
    private int oldQty;
    private int newQty;
    private String reason;
    private LocalDate date;
    private StockDetailManager manager;

    public StockDetails(String productName, int oldQty, int newQty, String reason) {
        this.productName = productName;
        this.oldQty = oldQty;
        this.newQty = newQty;
        this.reason = reason;
        date = LocalDate.now();

        int change;
        if (newQty == oldQty) {
            change = newQty;
        } else {
            change = newQty - oldQty;
        }
        manager = new StockDetailManager(reason,change);
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

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }
}
