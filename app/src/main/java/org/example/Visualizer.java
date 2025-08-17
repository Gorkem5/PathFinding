package org.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Visualizer {
    private final GraphicsContext gc;
    private final Canvas canvas;

    public String defaultRectHexColor = "#303033";
    public String defaultLineHexColor = "#dbdbdb";
    public String defaultDotHexColor = "#ffffff";

    public Visualizer(GraphicsContext gc, Canvas canvas) {
        this.gc = gc;
        this.canvas = canvas;
    }

    public void drawRect(double xPos, double yPos, double width, double height) {
        drawRect(xPos, yPos, width, height, defaultRectHexColor);
    }

    public void drawRect(double xPos, double yPos, double width, double height, String hexColor) {
        gc.setFill(Color.web(hexColor));
        gc.fillRect(xPos, yPos, width, height);
    }

    public void drawLine(double xPos1, double yPos1, double xPos2, double yPos2) {
        drawLine(xPos1, yPos1, xPos2, yPos2, 2, defaultLineHexColor);
    }

    public void drawLine(double xPos1, double yPos1, double xPos2, double yPos2, int lineWidth, String hexColor) {
        gc.setStroke(Color.web(hexColor));
        gc.setLineWidth(lineWidth);
        gc.strokeLine(xPos1, yPos1, xPos2, yPos2);
    }

    public void drawDot(double xPos, double yPos, double radius) {
        drawDot(xPos, yPos, radius, defaultDotHexColor);
    }

    public void drawDot(double xPos, double yPos, double radius, String hexColor) {
        gc.setFill(Color.web(hexColor));
        gc.fillOval(xPos - radius, yPos - radius, radius * 2, radius * 2);
    }


}