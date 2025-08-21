package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Slider;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.text.Text;

public class PathFindingController implements Initializable {

    @FXML
    private Canvas canvas;
    public Slider CalcSpeedSlider;
    public Text iterationCount;
    public ChoiceBox<String> pathFindingType;
    public TextField dotCount;
    public TextField lineCount;

    private MapGenerator mapGenerator;
    private Visualizer vs;
    private int dotAmount = 300;
    private int lineTries = 15000;
    private final float timeToWatch = 50;
    private BFS bfs;
    private DFS dfs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        vs = new Visualizer(gc, canvas);

        //      Set defaults
        pathFindingType.getSelectionModel().selectFirst();
        dotCount.setText(String.valueOf(dotAmount));
        lineCount.setText(String.valueOf(lineTries));

        mapGenerator = new MapGenerator(vs, canvas.getWidth(), canvas.getHeight());
        mapGenerator.generateMap(dotAmount, lineTries);
    }

    @FXML
    public void generateMap(ActionEvent actionEvent) {

        if(bfs != null) bfs.interrupt();
        if(dfs != null) dfs.interrupt();
        dotAmount = Integer.parseInt(dotCount.getText());
        lineTries = Integer.parseInt(lineCount.getText());


        mapGenerator.generateMap(dotAmount, lineTries);
    }

    @FXML
    public void findPath(ActionEvent actionEvent) {
        double calculationSpeed = CalcSpeedSlider.getValue();
        TextUpdater textUpdater = new TextUpdater(iterationCount, 4);
        String selectedPathFindingType = (String) pathFindingType.getValue();
    bfs = new BFS(vs, mapGenerator.getDotMap(), mapGenerator.getRoadList(),  1, dotAmount, (int) (timeToWatch * (1 - calculationSpeed / 100) / mapGenerator.getRoadList().size() * 1000), textUpdater,"BFS thread");
    dfs = new DFS(vs, mapGenerator.getDotMap(), mapGenerator.getRoadList(),  1, dotAmount, (int) (timeToWatch * (1 - calculationSpeed / 100) / mapGenerator.getRoadList().size() * 1000), textUpdater,"DFS thread");

        switch (selectedPathFindingType) {
            case "BFS" -> bfs.start();
            case "DFS" -> dfs.start();
            default -> {}
        }
    }
}