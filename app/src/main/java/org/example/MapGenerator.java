package org.example;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;


public class MapGenerator {

    private final Visualizer vs;

    private final double xCanvasWidth;
    private final double yCanvasWidth;

    private Map<Integer, SimpleEntry<Double, Double>> dotMap = new HashMap<>(); // x, y
    private record RoadData(int dot1, int dot2, double length) {}
    private List<RoadData> roadList = new ArrayList<>();
    private Set<Integer> dotHavePath = new HashSet<>();

    // reuse a single Random to avoid creating many instances
    private final Random rand = new Random();

    MapGenerator(Visualizer vs, double xCanvasWidth, double yCanvasWidth) {
        this.vs = vs;
        this.xCanvasWidth = xCanvasWidth;
        this.yCanvasWidth = yCanvasWidth;
    }

    public void generateMap(int dotAmount, int lineTries) {
        dotMap = new HashMap<>(); // x, y
        roadList = new ArrayList<>();
        dotHavePath = new HashSet<>();

        vs.drawRect(0f, 0f, xCanvasWidth, yCanvasWidth); // set canvas bg


        for (int i = 1; i <= dotAmount; i++) {
            double xPos = randomNum(0, xCanvasWidth);
            double yPos = randomNum(0, yCanvasWidth);

            dotMap.put(i, new SimpleEntry<>(xPos, yPos));
            vs.drawDot(xPos, yPos, 4);
        }


        for (int i = 0; i < lineTries; i++) {
            int randomDot1 = randomNum(1, dotAmount);
            int randomDot2 = randomNum(1, dotAmount);

            while(randomDot1 == randomDot2) randomDot2 = randomNum(1, dotAmount);

            drawLine(randomDot1, randomDot2);
        }

        for (int i = 1; i <= dotAmount; i++) {
            if(!dotHavePath.contains(i)){
                boolean haveLine = false;
                for (int j = 1; j <= dotAmount; j++) {
                    if(i != j)
                        if(drawLine(i, j)){
                            haveLine = true;
                            break;
                        }
                }

                if(!haveLine) vs.drawDot(dotMap.get(i).getKey(), dotMap.get(i).getValue(), 4, vs.defaultRectHexColor);
            }
        }
    }

    public boolean drawLine(int dot1, int dot2){
        double x1 = dotMap.get(dot1).getKey();
        double y1 = dotMap.get(dot1).getValue();
        double x2 = dotMap.get(dot2).getKey();
        double y2 = dotMap.get(dot2).getValue();

        boolean isIntersectiong = false;
        for (int j = 0; j < roadList.size(); j++) {
            int dot3 = roadList.get(j).dot1();
            int dot4 = roadList.get(j).dot2();

            double x3 = dotMap.get(dot3).getKey();
            double y3 = dotMap.get(dot3).getValue();
            double x4 = dotMap.get(dot4).getKey();
            double y4 = dotMap.get(dot4).getValue();

            if(lineIntersect(x1, y1, x2, y2, x3, y3, x4, y4)) {
                isIntersectiong = true;
                break;
            }
        }

        if(!isIntersectiong){
            double length = Math.hypot(x2 - x1, y2 - y1);
            roadList.add(new RoadData(dot1, dot2, length));
            dotHavePath.add(dot1);
            dotHavePath.add(dot2);
            vs.drawLine(x1, y1, x2, y2);
            return true;
        }

        return false;
    }

    public float randomNum(float min, float max) {
    return (float) (min + rand.nextFloat() * (max - min));
    }

    public double randomNum(double min, double max) {
    return min + rand.nextDouble() * (max - min);
    }
    public int randomNum(int min, int max) {
    
    if (max <= min) return min;
    return min + rand.nextInt((max - min) + 1);
    }

    public boolean lineIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        int o1 = orientation(x1, y1, x2, y2, x3, y3);
        int o2 = orientation(x1, y1, x2, y2, x4, y4);
        int o3 = orientation(x3, y3, x4, y4, x1, y1);
        int o4 = orientation(x3, y3, x4, y4, x2, y2);

        if(isSameDot(x1, y1, x3, y3) || isSameDot(x1, y1, x4, y4) || isSameDot(x2, y2, x3, y3) || isSameDot(x2, y2, x4, y4)) return false;
        return o1 != o2 && o3 != o4;
    }

    private int orientation(double ax, double ay, double bx, double by, double cx, double cy) {
        double val = (by - ay) * (cx - bx) - (bx - ax) * (cy - by);
        if (val == 0) return 0;           // collinear
        return (val > 0) ? 1 : 2;         // 1 - clock, 2 - against clock
    }

    private boolean isSameDot(double x1, double y1, double x2, double y2) {
        return x1 == x2 && y1 == y2;
    }


}