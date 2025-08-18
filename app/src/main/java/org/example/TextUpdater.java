package org.example;

import javafx.application.Platform;
import javafx.scene.text.Text;

public class TextUpdater {
    private final Text textNode;
    private final int symbolTarget;

    public TextUpdater(Text textNode) {
        this(textNode, 4);
    }

    public TextUpdater(Text textNode, int symbolTarget) {
        this.textNode = textNode;
        this.symbolTarget = symbolTarget;
    }

    public void setText(String value) {
        Platform.runLater(() -> {
            StringBuilder zeros = new StringBuilder();
            if (symbolTarget > value.length())
                zeros.append("0".repeat(symbolTarget - value.length()));

            textNode.setText(zeros + value);
        });
    }
}