package org.example;

import java.io.Console;
import java.util.*;

public class DFS extends Thread{
    private final Visualizer vs;
    private Map<Integer, AbstractMap.SimpleEntry<Double, Double>> dotMap = new HashMap<>(); // x, y

    private List<RoadData> roadList = new ArrayList<>();

    private final int startDot;
    private final int endDot;

    private final int sleepTime;
    private final TextUpdater textUpdater;
    private int iterationCount = 0;

    private Map<Integer, ArrayList<Integer>> pathToDot = new HashMap<>();

    DFS(Visualizer vs, Map<Integer, AbstractMap.SimpleEntry<Double, Double>> dotMap, List<RoadData> roadList, int startDot, int endDot, int sleepTime, TextUpdater textUpdater, String name) {
        super(name);
        this.vs = vs;
        this.dotMap = dotMap;
        this.roadList = roadList;
        this.startDot = startDot;
        this.endDot = endDot;
        this.sleepTime = sleepTime;
        this.textUpdater = textUpdater;
    }

    public void run() {
        Set<Integer> visited = new HashSet<>();

        Deque<Integer> deque = new ArrayDeque<>();
        deque.addLast(startDot);
        visited.add(startDot);
        pathToDot.put(startDot, new ArrayList<>(List.of(startDot)));

        int current = 0;
        while(current != endDot && !visited.contains(endDot)) {
            if(!deque.isEmpty())
                current = deque.pollLast();
            else return;

            vs.drawDot(dotMap.get(current).getKey(), dotMap.get(current).getValue(), 5, "#FFFF00");

            for (RoadData roadData : roadList) {
                int dot1 = roadData.dot1();
                int dot2 = roadData.dot2();
                if (dot1 == current || dot2 == current) {
                    int next = dot1 == current ? dot2 : dot1;
                    vs.drawLine(dotMap.get(current).getKey(), dotMap.get(current).getValue(), dotMap.get(next).getKey(), dotMap.get(next).getValue(), 5,"#000000");
                    textUpdater.setText(String.valueOf(iterationCount++));

                    if(!pathToDot.containsKey(next)) {
                        ArrayList<Integer> newPath = new ArrayList<>(pathToDot.get(current));
                        newPath.add(next);
                        pathToDot.put(next, newPath);
                    }


                    if (!visited.contains(next)) {
                        deque.addLast(next);
                        visited.add(next);

                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }

                }

            }
        }

        ArrayList<Integer> path = new ArrayList<>(pathToDot.get(endDot));
        for (int i = 1; i < path.size(); i++) {
            int prev = path.get(i-1);
            int cur = path.get(i);
            vs.drawLine(dotMap.get(prev).getKey(), dotMap.get(prev).getValue(), dotMap.get(cur).getKey(), dotMap.get(cur).getValue(), 5,"#008000");
        }
    }


}