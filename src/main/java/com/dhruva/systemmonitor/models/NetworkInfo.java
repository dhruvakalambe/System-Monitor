package com.dhruva.systemmonitor.models;

public class NetworkInfo {

    private final double downloadSpeed;
    private final double uploadSpeed;

    public NetworkInfo(double downloadSpeed, double uploadSpeed) {
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
    }

    public double getDownloadSpeed() {
        return downloadSpeed;
    }

    public double getUploadSpeed() {
        return uploadSpeed;
    }
}