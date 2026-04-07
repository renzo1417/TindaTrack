package com.bigo.tindatrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DashboardApplication.class.getResource("Dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMinWidth(1500);
        stage.setMinHeight(950);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
