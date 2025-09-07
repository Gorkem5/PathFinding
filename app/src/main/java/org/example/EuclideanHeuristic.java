package org.example;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

/**
 * Straight-line distance heuristic between current and goal nodes.
 */
public class EuclideanHeuristic implements Heuristic {
    private final Map<Integer, SimpleEntry<Double, Double>> dotMap;

    public EuclideanHeuristic(Map<Integer, SimpleEntry<Double, Double>> dotMap) {
        this.dotMap = dotMap;
    }

    @Override
    public double estimate(int current, int goal) {
        var a = dotMap.get(current);
        var b = dotMap.get(goal);
        if (a == null || b == null) return 0.0;
        double dx = a.getKey() - b.getKey();
        double dy = a.getValue() - b.getValue();
        double d = Math.sqrt(dx * dx + dy * dy);
        return Double.isFinite(d) && d >= 0 ? d : 0.0;
    }
}
