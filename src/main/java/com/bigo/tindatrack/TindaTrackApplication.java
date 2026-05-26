package com.bigo.tindatrack;

import com.bigo.tindatrack.SQLite_Database.NotificationManagement.NotificationPreferencesDAO;
import com.bigo.tindatrack.SQLite_Database.SalesManagement.SalesTableManagement;
import com.bigo.tindatrack.SQLite_Database.StockManagement.StockTableManagement;
import com.bigo.tindatrack.SQLite_Database.productsManagement.ProductManagement;
import com.bigo.tindatrack.SQLite_Database.productsManagement.productTableManagement;
import com.bigo.tindatrack.SQLite_Database.userManagement.UsersTableManagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class TindaTrackApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        initDatabase();
        FXMLLoader fxmlLoader = new FXMLLoader(TindaTrackApplication.class.getResource("Login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMinWidth(1500);
        stage.setMinHeight(950);
        stage.setTitle("TindaTrack");
        stage.setScene(scene);
        stage.show();

    }

    // creates the table
    private void initDatabase() {
        SalesTableManagement.createSalesTable();
        UsersTableManagement.createUserTable();
        productTableManagement.createProductTable();
        ProductManagement.createNotificationsTable();
        StockTableManagement.createStockTable();
        NotificationPreferencesDAO.createTable();
    }
}

