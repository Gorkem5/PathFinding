package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

import org.example.Visualizer;
import org.example.MapGenerator;
import javafx.scene.text.Text;

public class PathFindingController implements Initializable {

    @FXML
    private Canvas canvas;

    public Slider CalcSpeedSlider;
    public Text iterationCount;

    private MapGenerator mapGenerator;
    private Visualizer vs;
    private final int dotAmount = 300;
    private final int lineTries = 15000;
    private final float timeToWatch = 50;
    private BFS bfs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        vs = new Visualizer(gc, canvas);

        mapGenerator = new MapGenerator(vs, canvas.getWidth(), canvas.getHeight());
        mapGenerator.generateMap(dotAmount, lineTries);
    }

    @FXML
    public void generateMap(ActionEvent actionEvent) {
        bfs.interrupt();
        mapGenerator.generateMap(dotAmount, lineTries);
    }

    @FXML
    public void findPath(ActionEvent actionEvent) {
        double calculationSpeed = CalcSpeedSlider.getValue();
        TextUpdater textUpdater = new TextUpdater(iterationCount, 4);
        bfs = new BFS(vs, mapGenerator.getDotMap(), mapGenerator.getRoadList(),  1, dotAmount, (int) (timeToWatch * (1 - calculationSpeed / 100) / mapGenerator.getRoadList().size() * 1000), textUpdater,"BFS thread");
        bfs.start();
    }
}