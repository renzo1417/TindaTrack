package com.bigo.tindatrack.Controller.Inventory.InventoryActionController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;


public class ActionController {
    @FXML
    private Pane actionPane;
    @FXML
    private Button trashButton;

    public Pane getActionPane() {
        return actionPane;
    }

    public Button getTrashButton() {
        return trashButton;
    }
}
