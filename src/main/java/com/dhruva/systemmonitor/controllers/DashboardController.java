package com.dhruva.systemmonitor.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import java.io.IOException;

public class DashboardController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Button systemBtn;

    @FXML
    private Button performanceBtn;

    @FXML
    private Button processesBtn;

    @FXML
    public void initialize() {
        setActive(systemBtn);
        loadPage("system.fxml");   // Default page
    }

    private void setActive(Button activeButton) {

        systemBtn.getStyleClass().remove("nav-button-active");
        performanceBtn.getStyleClass().remove("nav-button-active");
        processesBtn.getStyleClass().remove("nav-button-active");

        activeButton.getStyleClass().add("nav-button-active");
    }

    @FXML
    private void showPerformance() {
        setActive(performanceBtn);
        loadPage("performance.fxml");
    }

    @FXML
    private void showProcesses() {
        setActive(processesBtn);
        loadPage("processes.fxml");
    }

    @FXML
    private void showSystem() {
        setActive(systemBtn);
        loadPage("system.fxml");
    }

    private void loadPage(String fxmlFile) {

        try {

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    FXMLLoader.load(getClass().getResource("/fxml/" + fxmlFile))
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}