package com.dhruva.systemmonitor.controllers;

import com.dhruva.systemmonitor.models.*;
import com.dhruva.systemmonitor.services.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import com.dhruva.systemmonitor.controllers.components.ExpandableCardController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PerformanceController {

    @FXML
    private VBox cardContainer;

    @FXML
    public void initialize() {

        createMemoryCard();
        startMemoryUpdates();

        createCpuCard();
        startCpuUpdates();

        createDiskCard();
        startDiskUpdates();

        createNetworkCard();
        startNetworkUpdates();

    }

    private ExpandableCardController memoryCard;
    private ExpandableCardController cpuCard;
    private ExpandableCardController diskCard;
    private ExpandableCardController networkCard;


    private final CPUService cpuService = new CPUService();
    private final DiskService diskService = new DiskService();
    private final NetworkService networkService = new NetworkService();

    // Memory
    private void createMemoryCard() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/components/ExpandableCard.fxml"));

            VBox card = loader.load();

            card.setMaxWidth(Double.MAX_VALUE);

//            ExpandableCardController controller = loader.getController();
            memoryCard = loader.getController();

            MemoryInfo memory = new MemoryService().getMemoryInfo();

            double totalGB = memory.getTotalMemory() / 1024.0 / 1024.0;
            double usedGB = memory.getUsedMemory() / 1024.0 / 1024.0;
            double availableGB = memory.getAvailableMemory() / 1024.0 / 1024.0;

            memoryCard.setTitle("Memory");

            memoryCard.setSummary(
                    String.format("%.1f%%", memory.getUsagePercentage())
            );

            memoryCard.setLine1(
                    String.format("Used : %.2f GB", usedGB)
            );

            memoryCard.setLine2(
                    String.format("Available : %.2f GB", availableGB)
            );

            memoryCard.setLine3(
                    String.format("Total : %.2f GB", totalGB)
            );

            card.setMaxWidth(Double.MAX_VALUE);
            cardContainer.getChildren().add(card);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
    private void startMemoryUpdates() {

        Timeline timeline = new Timeline(

                new KeyFrame(Duration.seconds(1), event -> {

                    MemoryInfo memory = new MemoryService().getMemoryInfo();

                    double totalGB = memory.getTotalMemory() / 1024.0 / 1024.0;
                    double usedGB = memory.getUsedMemory() / 1024.0 / 1024.0;
                    double availableGB = memory.getAvailableMemory() / 1024.0 / 1024.0;

                    memoryCard.updateSummary(
                            String.format("%.1f%%", memory.getUsagePercentage())
                    );
                    memoryCard.setProgress(memory.getUsagePercentage());
                    memoryCard.addDataPoint(memory.getUsagePercentage());

                    memoryCard.updateLine1(
                            String.format("Used : %.2f GB", usedGB)
                    );

                    memoryCard.updateLine2(
                            String.format("Available : %.2f GB", availableGB)
                    );

                    memoryCard.updateLine3(
                            String.format("Total : %.2f GB", totalGB)
                    );

                })

        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // CPU
    private void createCpuCard() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/components/ExpandableCard.fxml"));

            VBox card = loader.load();

            card.setMaxWidth(Double.MAX_VALUE);


            cpuCard = loader.getController();

            CPUInfo cpu = cpuService.getCPUInfo();

            cpuCard.setTitle("CPU");

            cpuCard.setSummary(
                    String.format("%.1f%%", cpu.getUsagePercentage())
            );

            cpuCard.setLine1("Live CPU Usage");

            card.setMaxWidth(Double.MAX_VALUE);
            cardContainer.getChildren().add(card);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void startCpuUpdates() {

        Timeline timeline = new Timeline(

                new KeyFrame(Duration.seconds(1), event -> {

                    CPUInfo cpu = cpuService.getCPUInfo();

                    cpuCard.updateSummary(
                            String.format("%.1f%%", cpu.getUsagePercentage())
                    );
                    cpuCard.setProgress(cpu.getUsagePercentage());
                    cpuCard.addDataPoint(cpu.getUsagePercentage());

                    cpuCard.updateLine1(
                            String.format("Current Usage : %.1f%%",
                                    cpu.getUsagePercentage())
                    );

                    cpuCard.updateLine2(
                            "Source : /proc/stat"
                    );

                })

        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    //Disk
    private void createDiskCard() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/components/ExpandableCard.fxml"));

            VBox card = loader.load();

            card.setMaxWidth(Double.MAX_VALUE);

            diskCard = loader.getController();

            DiskInfo disk = diskService.getDiskInfo();

            double totalGB = disk.getTotalSpace() / 1024.0 / 1024 / 1024;
            double usedGB = disk.getUsedSpace() / 1024.0 / 1024 / 1024;
            double freeGB = disk.getFreeSpace() / 1024.0 / 1024 / 1024;

            diskCard.setTitle("Disk");

            diskCard.setSummary(
                    String.format("%.1f%%", disk.getUsagePercentage())
            );

            diskCard.setLine1(
                    String.format("Used : %.2f GB", usedGB)
            );

            diskCard.setLine2(
                    String.format("Free : %.2f GB", freeGB)
            );

            diskCard.setLine3(
                    String.format("Total : %.2f GB", totalGB)
            );

            card.setMaxWidth(Double.MAX_VALUE);
            cardContainer.getChildren().add(card);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
    private void startDiskUpdates() {

        Timeline timeline = new Timeline(

                new KeyFrame(Duration.seconds(1), event -> {

                    DiskInfo disk = diskService.getDiskInfo();

                    double totalGB = disk.getTotalSpace() / 1024.0 / 1024 / 1024;
                    double usedGB = disk.getUsedSpace() / 1024.0 / 1024 / 1024;
                    double freeGB = disk.getFreeSpace() / 1024.0 / 1024 / 1024;

                    diskCard.updateSummary(
                            String.format("%.1f%%", disk.getUsagePercentage())
                    );
                    diskCard.setProgress(disk.getUsagePercentage());
                    diskCard.addDataPoint(disk.getUsagePercentage());

                    diskCard.updateLine1(
                            String.format("Used : %.2f GB", usedGB)
                    );

                    diskCard.updateLine2(
                            String.format("Free : %.2f GB", freeGB)
                    );

                    diskCard.updateLine3(
                            String.format("Total : %.2f GB", totalGB)
                    );

                })

        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    //Network
    private void createNetworkCard() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/components/ExpandableCard.fxml"));

            VBox card = loader.load();

            card.setMaxWidth(Double.MAX_VALUE);


            networkCard = loader.getController();

            NetworkInfo network = networkService.getNetworkInfo();

            double total = network.getDownloadSpeed() + network.getUploadSpeed();

            networkCard.setTitle("Network");

            networkCard.setSummary(
                    String.format("%.2f KB/s", total)
            );

            networkCard.setLine1(
                    String.format("Download : %.2f KB/s",
                            network.getDownloadSpeed())
            );

            networkCard.setLine2(
                    String.format("Upload : %.2f KB/s",
                            network.getUploadSpeed())
            );

            networkCard.setLine3(
                    "Live Network Speed"
            );

            card.setMaxWidth(Double.MAX_VALUE);
            cardContainer.getChildren().add(card);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
    private void startNetworkUpdates() {

        Timeline timeline = new Timeline(

                new KeyFrame(Duration.seconds(1), e -> {

                    NetworkInfo network = networkService.getNetworkInfo();

                    double total = network.getDownloadSpeed()
                            + network.getUploadSpeed();

                    networkCard.updateSummary(
                            String.format("%.2f KB/s", total));
                    networkCard.addDataPoint(Math.min(total, 100));

                    networkCard.updateLine1(
                            String.format("Download : %.2f KB/s",
                                    network.getDownloadSpeed()));

                    networkCard.updateLine2(
                            String.format("Upload : %.2f KB/s",
                                    network.getUploadSpeed()));

                })

        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}