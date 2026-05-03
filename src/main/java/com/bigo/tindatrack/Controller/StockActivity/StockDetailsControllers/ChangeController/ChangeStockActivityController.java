package com.bigo.tindatrack.Controller.StockActivity.StockDetailsControllers.ChangeController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.javafx.FontIcon;

public class ChangeStockActivityController {
    @FXML
    private Pane changePane;
    @FXML
    private FontIcon decreaseIcon;
    @FXML
    private FontIcon increaseIcon;
    @FXML
    private Label detailLabel;

    public Pane getChangePane() {
        return changePane;
    }

    public FontIcon getDecreaseIcon() {
        return decreaseIcon;
    }

    public FontIcon getIncreaseIcon() {
        return increaseIcon;
    }

    public Label getDetailLabel() {
        return detailLabel;
    }
}
