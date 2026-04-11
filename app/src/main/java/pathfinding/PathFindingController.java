package pathfinding;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pathfinding.algorithms.AStar;
import pathfinding.algorithms.BFS;
import pathfinding.algorithms.DFS;
import pathfinding.ui.TextUpdater;
import pathfinding.ui.Visualizer;
import pathfinding.utils.MapGenerator;

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
    private int lineTries = 600;
    private final float timeToWatch = 50;
    private AStar aStar;
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
        if(aStar != null) aStar.interrupt();
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
        aStar = new AStar(vs, mapGenerator.getDotMap(), mapGenerator.getRoadList(), 1, dotAmount, (int) (timeToWatch * (1 - calculationSpeed / 100) / mapGenerator.getRoadList().size() * 1000), textUpdater, null, "A* thread");
        System.out.println(selectedPathFindingType);
        switch (selectedPathFindingType) {
            case "BFS" -> bfs.start();
            case "DFS" -> dfs.start();
            case "A*" -> aStar.start();
        }

    }


}
