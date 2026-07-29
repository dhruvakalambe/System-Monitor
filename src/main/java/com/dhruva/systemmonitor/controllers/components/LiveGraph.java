package com.dhruva.systemmonitor.components;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LiveGraph extends LineChart<Number, Number> {

    private static final int MAX_POINTS = 60;

    private final XYChart.Series<Number, Number> series =
            new XYChart.Series<>();

    private int x = 0;

    public LiveGraph() {

        super(new NumberAxis(), new NumberAxis());

        NumberAxis xAxis = (NumberAxis) getXAxis();
        NumberAxis yAxis = (NumberAxis) getYAxis();

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(MAX_POINTS);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        yAxis.setTickLabelsVisible(false);
        yAxis.setTickMarkVisible(false);
        yAxis.setMinorTickVisible(false);

        setLegendVisible(false);
        setAnimated(false);
        setCreateSymbols(false);

        getData().add(series);

        setPrefHeight(140);

        getStyleClass().add("live-chart");
    }

    public void addValue(double value) {

        if (series.getData().size() >= MAX_POINTS) {
            series.getData().remove(0);
        }

        series.getData().add(new XYChart.Data<>(x++, value));

        ((NumberAxis) getXAxis()).setLowerBound(
                Math.max(0, x - MAX_POINTS));

        ((NumberAxis) getXAxis()).setUpperBound(
                Math.max(MAX_POINTS, x));
    }

}