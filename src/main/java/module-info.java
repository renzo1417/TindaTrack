module com.bigo.tindatrack {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.materialdesign2;
    requires java.sql;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;
//    requires com.bigo.tindatrack;

    opens com.bigo.tindatrack to javafx.fxml;
    exports com.bigo.tindatrack;
    exports com.bigo.tindatrack.Controller;
    opens com.bigo.tindatrack.Controller to javafx.fxml;
    exports com.bigo.tindatrack.Controller.Inventory;
    opens com.bigo.tindatrack.Controller.Inventory to javafx.fxml;
    exports com.bigo.tindatrack.Controller.Inventory.AddProductController;
    opens com.bigo.tindatrack.Controller.Inventory.AddProductController to javafx.fxml;
    exports com.bigo.tindatrack.Controller.Inventory.ModifyProductController;
    opens com.bigo.tindatrack.Controller.Inventory.ModifyProductController to javafx.fxml;
    exports com.bigo.tindatrack.Controller.Inventory.InventoryActionController;
    opens com.bigo.tindatrack.Controller.Inventory.InventoryActionController to javafx.fxml;
    exports com.bigo.tindatrack.Product;
    opens com.bigo.tindatrack.Product to javafx.fxml;
    exports com.bigo.tindatrack.utils;
    opens com.bigo.tindatrack.utils to javafx.fxml;
}