module com.bigo.tindatrack {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.materialdesign2;
    requires java.sql;
    requires java.desktop;

    opens com.bigo.tindatrack to javafx.fxml;
    exports com.bigo.tindatrack;
    exports com.bigo.tindatrack.Controller;
    opens com.bigo.tindatrack.Controller to javafx.fxml;
    exports com.bigo.tindatrack.Controller.Inventory;
    opens com.bigo.tindatrack.Controller.Inventory to javafx.fxml;
    exports com.bigo.tindatrack.Controller.Inventory.AddProductController;
    opens com.bigo.tindatrack.Controller.Inventory.AddProductController to javafx.fxml;
}