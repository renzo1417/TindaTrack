package com.bigo.tindatrack;

import com.bigo.tindatrack.SQLite_Database.TableManagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class TindaTrackApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TindaTrackApplication.class.getResource("Login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMinWidth(1500);
        stage.setMinHeight(950);
        stage.setTitle("TindaTrack");
        stage.setScene(scene);
        stage.show();




    }
}
