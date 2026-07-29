package com.dhruva.systemmonitor.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import com.dhruva.systemmonitor.components.LiveGraph;
import javafx.scene.layout.StackPane;

public class ExpandableCardController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label line1;

    @FXML
    private Label line2;

    @FXML
    private Label line3;

    @FXML
    private VBox detailsBox;

    @FXML
    private Button expandButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private StackPane graphContainer;

    private final LiveGraph liveGraph = new LiveGraph();

    private boolean expanded = false;

    @FXML
    private void toggle() {

        expanded = !expanded;

        detailsBox.setVisible(expanded);
        detailsBox.setManaged(expanded);

        expandButton.setText(expanded ? "▲" : "▼");
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setSummary(String summary) {
        summaryLabel.setText(summary);
    }

    public void setLine1(String text) {
        line1.setText(text);
    }

    public void setLine2(String text) {
        line2.setText(text);
    }

    public void setLine3(String text) {
        line3.setText(text);
    }

    public void updateSummary(String text) {
        summaryLabel.setText(text);
    }

    public void updateLine1(String text) {
        line1.setText(text);
    }

    public void updateLine2(String text) {
        line2.setText(text);
    }

    public void updateLine3(String text) {
        line3.setText(text);
    }

    public void setProgress(double percentage) {

        progressBar.setProgress(percentage / 100.0);

        progressBar.getStyleClass().removeAll(
                "progress-good",
                "progress-warning",
                "progress-danger"
        );

        if (percentage < 50) {

            progressBar.getStyleClass().add("progress-good");

        } else if (percentage < 80) {

            progressBar.getStyleClass().add("progress-warning");

        } else {

            progressBar.getStyleClass().add("progress-danger");

        }
    }

    @FXML
    public void initialize() {

        graphContainer.getChildren().add(liveGraph);

    }

    public void addDataPoint(double value) {

        liveGraph.addValue(value);

    }
}