package com.dhruva.systemmonitor.models;

public class DiskInfo {

    private final long totalSpace;
    private final long freeSpace;
    private final long usedSpace;
    private final double usagePercentage;

    public DiskInfo(long totalSpace, long freeSpace) {

        this.totalSpace = totalSpace;
        this.freeSpace = freeSpace;
        this.usedSpace = totalSpace - freeSpace;

        if (totalSpace == 0) {
            usagePercentage = 0;
        } else {
            usagePercentage = ((double) usedSpace / totalSpace) * 100;
        }

    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public long getUsedSpace() {
        return usedSpace;
    }

    public double getUsagePercentage() {
        return usagePercentage;
    }
}