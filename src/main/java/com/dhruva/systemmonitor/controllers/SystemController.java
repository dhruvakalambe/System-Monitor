package com.dhruva.systemmonitor.controllers;

import com.dhruva.systemmonitor.models.SystemInfo;
import com.dhruva.systemmonitor.services.SystemInfoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class SystemController {

    @FXML
    private Label hostnameLabel;

    @FXML
    private Label osLabel;

    @FXML
    private Label kernelLabel;

    @FXML
    private Label architectureLabel;

    @FXML
    private Label cpuLabel;

    @FXML
    private Label coresLabel;

    @FXML
    private Label memoryLabel;

    @FXML
    private Label gpuLabel;

    @FXML
    private Label bootTimeLabel;

    @FXML
    private Label uptimeLabel;

    private final SystemInfoService service =
            new SystemInfoService();

    private Timeline uptimeTimeline;

    private void startUptimeTimer() {

        uptimeTimeline = new Timeline(

                new KeyFrame(Duration.seconds(1), event ->

                        uptimeLabel.setText(service.getCurrentUptime())

                )

        );

        uptimeTimeline.setCycleCount(Timeline.INDEFINITE);

        uptimeTimeline.play();

    }

    @FXML
    public void initialize() {

        SystemInfo info = service.getSystemInfo();

        hostnameLabel.setText(info.getHostname());
        osLabel.setText(info.getOperatingSystem());
        kernelLabel.setText(info.getKernel());
        architectureLabel.setText(info.getArchitecture());
        cpuLabel.setText(info.getCpu());
        coresLabel.setText(String.valueOf(info.getCpuCores()));
        memoryLabel.setText(info.getMemory());
        gpuLabel.setText(info.getGpu());
        bootTimeLabel.setText(info.getBootTime());
        uptimeLabel.setText(info.getUptime());
        startUptimeTimer();

    }

}