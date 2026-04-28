package com.bigo.tindatrack.Product;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class ProductStatusController {
    @FXML
    private Pane statusPane;
    @FXML
    private Label statusLabel;
    @FXML
    private Circle statusColor;

    public Pane getStatusPane() {
        return statusPane;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public Circle getStatusColor() {
        return statusColor;
    }
}
