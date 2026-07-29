package com.dhruva.systemmonitor.controllers;

import com.dhruva.systemmonitor.models.ProcessInfo;
import com.dhruva.systemmonitor.services.ProcessService;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.dhruva.systemmonitor.utils.MemoryFormatter;
import com.dhruva.systemmonitor.services.ProcessMemoryService;
import com.dhruva.systemmonitor.services.ProcessCPUService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.List;

public class ProcessesController {

    @FXML
    private TableView<ProcessInfo> processTable;

    @FXML
    private TableColumn<ProcessInfo, Long> pidColumn;

    @FXML
    private TableColumn<ProcessInfo, String> nameColumn;

    @FXML
    private TableColumn<ProcessInfo, Long> memoryColumn;

    @FXML
    private TableColumn<ProcessInfo, Double> cpuColumn;

    @FXML
    private TextField searchField;

    private final ObservableList<ProcessInfo> processList = FXCollections.observableArrayList();
    private final ProcessMemoryService memoryService = new ProcessMemoryService();
    private final ProcessCPUService cpuService = new ProcessCPUService();
    private final ProcessService processService = new ProcessService(memoryService, cpuService);

    @FXML
    public void initialize() {

        pidColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getPid()).asObject());

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        processTable.setItems(processList);

        refreshProcesses();

        memoryColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getMemoryKB()).asObject());


        memoryColumn.setCellFactory(column -> new TableCell<>() {

            @Override
            protected void updateItem(Long memoryKB, boolean empty) {

                super.updateItem(memoryKB, empty);

                if (empty || memoryKB == null) {
                    setText(null);
                    return;
                }

                setText(MemoryFormatter.formatKB(memoryKB));

            }

        });

        cpuColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(
                        cellData.getValue().getCpuUsage()
                ).asObject());

        cpuColumn.setCellFactory(column -> new TableCell<>() {

            @Override
            protected void updateItem(Double cpuUsage, boolean empty) {

                super.updateItem(cpuUsage, empty);

                if (empty || cpuUsage == null) {
                    setText(null);
                    return;
                }

                setText(String.format("%.1f%%", cpuUsage));

            }

        });

        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        //Right Click Context Menu
        MenuItem terminateItem = new MenuItem("Terminate");
        MenuItem killItem = new MenuItem("Force Kill");

        ContextMenu contextMenu = new ContextMenu(
                terminateItem,
                killItem
        );

        processTable.setContextMenu(contextMenu);

        //Actions for Context Menu
        //Terminate
        terminateItem.setOnAction(event -> {

            ProcessInfo selectedProcess =
                    processTable.getSelectionModel().getSelectedItem();

            if (selectedProcess == null) {
                return;
            }

            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            alert.setTitle("Terminate Process");
            alert.setHeaderText("Terminate " + selectedProcess.getName() + "?");
            alert.setContentText(
                    "PID: " + selectedProcess.getPid()
            );

            alert.showAndWait().ifPresent(response -> {

                if (response == ButtonType.OK) {

                    boolean success = processService.terminateProcess(
                            selectedProcess.getPid()
                    );

                    System.out.println("Success: " + success);

                }

            });

        });
        //Force Kill
        killItem.setOnAction(event -> {

            ProcessInfo selectedProcess =
                    processTable.getSelectionModel().getSelectedItem();

            if (selectedProcess == null) {
                return;
            }

            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            alert.setTitle("Force Kill Process");

            alert.setHeaderText(
                    "Force kill " + selectedProcess.getName() + "?"
            );

            alert.setContentText(
                    "PID: " + selectedProcess.getPid() +
                            "\n\nThis will immediately terminate the process without allowing it to clean up or save its state."
            );

            alert.showAndWait().ifPresent(response -> {

                if (response == ButtonType.OK) {

                    boolean success = processService.killProcess(
                            selectedProcess.getPid()
                    );

                    System.out.println("Force Kill: " + success);

                    refreshProcesses();

                }

            });

        });

        //Timeline
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> refreshProcesses())
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //Search
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            refreshProcesses();
        });
    }

    private void refreshProcesses() {

        ProcessInfo selected =
                processTable.getSelectionModel().getSelectedItem();

        Long selectedPid = selected == null ? null : selected.getPid();

        List<ProcessInfo> processes = processService.getProcesses();

        String search = searchField.getText();

        if (search != null && !search.isBlank()) {

            processes.removeIf(process ->
                    !process.getName().toLowerCase()
                            .contains(search.toLowerCase()));

        }

        processList.setAll(processes);

        processTable.sort();

        if (selectedPid != null) {

            for (ProcessInfo process : processList) {

                if (process.getPid() == selectedPid) {

                    processTable.getSelectionModel().select(process);
                    break;

                }

            }

        }

    }

}