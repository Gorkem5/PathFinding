package org.example;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * A* pathfinding using a pluggable Heuristic. Visualization mirrors BFS/DFS.
 */
public class AStar extends Thread {
    private final Visualizer vs;
    private final Map<Integer, SimpleEntry<Double, Double>> dotMap; // x, y
    private final List<RoadData> roadList;

    private final int startDot;
    private final int endDot;
    private final int sleepTime;
    private final TextUpdater textUpdater;
    private final Heuristic heuristic;

    private int iterationCount = 0;

    public AStar(
            Visualizer vs,
            Map<Integer, SimpleEntry<Double, Double>> dotMap,
            List<RoadData> roadList,
            int startDot,
            int endDot,
            int sleepTime,
            TextUpdater textUpdater,
            Heuristic heuristic,
            String name
    ) {
        super(name);
        this.vs = vs;
        this.dotMap = dotMap;
        this.roadList = roadList;
        this.startDot = startDot;
        this.endDot = endDot;
        this.sleepTime = sleepTime;
        this.textUpdater = textUpdater;
        this.heuristic = heuristic != null ? heuristic : new EuclideanHeuristic(dotMap);
    }

    @Override
    public void run() {
        // Build adjacency list: node -> list of (neighbor, length)
        Map<Integer, List<RoadData>> adjacency = new HashMap<>();
        for (RoadData rd : roadList) {
            adjacency.computeIfAbsent(rd.dot1(), k -> new ArrayList<>()).add(rd);
            adjacency.computeIfAbsent(rd.dot2(), k -> new ArrayList<>()).add(rd);
        }

        // gScore / fScore
        Map<Integer, Double> gScore = new HashMap<>();
        Map<Integer, Double> fScore = new HashMap<>();
        Map<Integer, Integer> cameFrom = new HashMap<>();

        // Initialize
        gScore.put(startDot, 0.0);
        fScore.put(startDot, heuristic.estimate(startDot, endDot));

        Comparator<Integer> byF = Comparator.comparingDouble(n -> fScore.getOrDefault(n, Double.POSITIVE_INFINITY));
        PriorityQueue<Integer> open = new PriorityQueue<>(byF);
        Set<Integer> inOpen = new HashSet<>();

        open.add(startDot);
        inOpen.add(startDot);

        Set<Integer> closed = new HashSet<>();

        while (!open.isEmpty()) {
            int current = open.poll();
            inOpen.remove(current);

            if (current == endDot) {
                drawPath(cameFrom);
                return;
            }

            closed.add(current);

            // Visualize current expansion
            var curPt = dotMap.get(current);
            if (curPt != null) {
                vs.drawDot(curPt.getKey(), curPt.getValue(), 5, "#FFFF00");
            }

            for (RoadData edge : adjacency.getOrDefault(current, Collections.emptyList())) {
                int neighbor = edge.dot1() == current ? edge.dot2() : edge.dot1();
                if (closed.contains(neighbor)) continue;

                // tentative gScore
                double tentative = gScore.getOrDefault(current, Double.POSITIVE_INFINITY) + edge.length();

                if (tentative < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentative);
                    double h = heuristic.estimate(neighbor, endDot);
                    if (!Double.isFinite(h) || h < 0) h = 0.0;
                    fScore.put(neighbor, tentative + h);

                    if (!inOpen.contains(neighbor)) {
                        open.add(neighbor);
                        inOpen.add(neighbor);
                    } else {
                        // Reinsert to update priority (Java PQ has no decrease-key)
                        open.remove(neighbor);
                        open.add(neighbor);
                    }

                    // Visualize edge relaxation
                    var np = dotMap.get(neighbor);
                    if (curPt != null && np != null) {
                        vs.drawLine(curPt.getKey(), curPt.getValue(), np.getKey(), np.getValue(), 5, "#000000");
                    }
                    if (textUpdater != null) textUpdater.setText(String.valueOf(iterationCount++));
                    try { Thread.sleep(sleepTime); } catch (InterruptedException ie) { return; }
                }
            }
        }
        // No path
    }

    private void drawPath(Map<Integer, Integer> cameFrom) {
        // Reconstruct from endDot backwards
        List<Integer> path = new ArrayList<>();
        Integer cur = endDot;
        if (!cameFrom.containsKey(cur) && cur != startDot) return; // unreachable
        path.add(cur);
        while (!Objects.equals(cur, startDot)) {
            cur = cameFrom.get(cur);
            if (cur == null) break;
            path.add(cur);
        }
        Collections.reverse(path);
        for (int i = 1; i < path.size(); i++) {
            int prev = path.get(i - 1);
            int nxt = path.get(i);
            var a = dotMap.get(prev);
            var b = dotMap.get(nxt);
            if (a != null && b != null) {
                vs.drawLine(a.getKey(), a.getValue(), b.getKey(), b.getValue(), 5, "#008000");
            }
        }
    }
}
