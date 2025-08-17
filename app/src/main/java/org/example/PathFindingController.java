package com.example.pathfindingjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.pathfindingjava.Visualizer;
import com.example.pathfindingjava.MapGenerator;

public class PathFindingController implements Initializable {

    @FXML
    private Canvas canvas;

    private MapGenerator mapGenerator;
    private final int dotAmount = 100;
    private final int lineTries = 500;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        Visualizer vs = new Visualizer(gc, canvas);

        mapGenerator = new MapGenerator(vs, canvas.getWidth(), canvas.getHeight());
        mapGenerator.generateMap(dotAmount, lineTries);
    }

    @FXML
    public void generateMap(ActionEvent actionEvent) {
        mapGenerator.generateMap(dotAmount, lineTries);
    }

    @FXML
    public void findPath(ActionEvent actionEvent) {
        System.out.println(2);
    }
}