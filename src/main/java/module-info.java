module com.bigo.tindatrack {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.materialdesign2;

    opens com.bigo.tindatrack to javafx.fxml;
    exports com.bigo.tindatrack;
}