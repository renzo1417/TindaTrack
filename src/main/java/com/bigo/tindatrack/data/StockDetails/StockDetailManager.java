package com.bigo.tindatrack.data.StockDetails;

import com.bigo.tindatrack.Controller.StockActivity.StockDetailsControllers.ChangeController.ChangeStockActivityController;
import com.bigo.tindatrack.Controller.StockActivity.StockDetailsControllers.ReasonController.ReasonStockActivityController;
import com.bigo.tindatrack.Product.Product;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class StockDetailManager {
    private String reason;
    private String change;

    private ChangeStockActivityController changeController;
    private ReasonStockActivityController reasonController;

    public StockDetailManager(String reason, int change) {
        this.reason = reason;
        this.change = change + "";

        try {
            FXMLLoader loader1 = new FXMLLoader(Product.class.getResource("/com/bigo/tindatrack/ChangeStockActivity-view.fxml"));
            FXMLLoader loader2 = new FXMLLoader(Product.class.getResource("/com/bigo/tindatrack/ReasonStockActivity.fxml"));

            Parent root1 = loader1.load();
            Parent root2 = loader2.load();

            changeController = loader1.getController();
            reasonController = loader2.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setChange(reason);
        setReasonStyle(reason);
    }

    public void setChange(String reason) {
        this.reason = reason;

        if (changeController == null) return;

        if ("Restocked".equalsIgnoreCase(reason)) {
            changeController.getChangePane().setStyle("-fx-background-color: #DFFCE6; -fx-background-radius: 5;");
            changeController.getDetailLabel().setStyle("-fx-font-weight: bold; -fx-text-fill: #37a345;");

            changeController.getIncreaseIcon().setVisible(true);
            changeController.getDecreaseIcon().setVisible(false);

        } else if ("Sold".equalsIgnoreCase(reason)) {
            changeController.getChangePane().setStyle("-fx-background-color: #FCE2E2; -fx-background-radius: 5;");
            changeController.getDetailLabel().setStyle("-fx-font-weight: bold; -fx-text-fill: #d3221e;");

            changeController.getIncreaseIcon().setVisible(false);
            changeController.getDecreaseIcon().setVisible(true);
        }

        changeController.getDetailLabel().setText(change);
    }

    public void setReasonStyle(String reason) {
        if (reasonController == null) return;

        if ("Sold".equalsIgnoreCase(reason)) {
            reasonController.getReasonPane().setStyle("-fx-background-color: #FDF3C5; -fx-background-radius: 5;");
            reasonController.getReasonLabel().setStyle("-fx-font-weight: bold; -fx-text-fill: #D57600;");
        } else if ("Restocked".equalsIgnoreCase(reason)) {
            reasonController.getReasonPane().setStyle("-fx-background-color: #DDEAFF; -fx-background-radius: 5;");
            reasonController.getReasonLabel().setStyle("-fx-font-weight: bold; -fx-text-fill: #0056D2;");
        }

        reasonController.getReasonLabel().setText(reason);
    }

    public Pane getChange() {
        return changeController.getChangePane();
    }

    public Pane getReason() {
        return reasonController.getReasonPane();
    }
}
