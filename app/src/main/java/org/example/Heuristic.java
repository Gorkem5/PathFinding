package org.example;

/**
 * A simple contract for pathfinding heuristics.
 * estimate(cur, goal) should be non-negative and fast (O(1)).
 */
public interface Heuristic {
    double estimate(int current, int goal);
}
