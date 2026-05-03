package com.bigo.tindatrack.Controller.StockActivity.StockDetailsControllers.ReasonController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class ReasonStockActivityController {
    @FXML
    private Pane reasonPane;
    @FXML
    private Label reasonLabel;

    public Pane getReasonPane() {
        return reasonPane;
    }

    public Label getReasonLabel() {
        return reasonLabel;
    }
}
