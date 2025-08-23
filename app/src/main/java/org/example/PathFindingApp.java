package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PathFindingApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var url = PathFindingApp.class.getResource("/path-finding-tab.fxml");
        if (url == null) {
            throw new IllegalStateException("FXML '/path-finding-tab.fxml' not found on classpath");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        stage.setTitle("Path Finding!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}