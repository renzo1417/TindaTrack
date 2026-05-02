package com.bigo.tindatrack.Controller.Inventory.InventoryActionController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;


public class ActionController {
    @FXML
    private Pane actionPane;
    @FXML
    private Button trashButton;
    @FXML
    private Button modifyButton;

    public Pane getActionPane() {
        return actionPane;
    }

    public Button getTrashButton() {
        return trashButton;
    }

    public Button getModifyButton() {
        return modifyButton;
    }
}
