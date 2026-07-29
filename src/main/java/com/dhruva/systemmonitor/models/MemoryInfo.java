package com.dhruva.systemmonitor.models;

public class MemoryInfo {

    private final long totalMemory;
    private final long availableMemory;

    public MemoryInfo(long totalMemory, long availableMemory) {
        this.totalMemory = totalMemory;
        this.availableMemory = availableMemory;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public long getAvailableMemory() {
        return availableMemory;
    }

    public long getUsedMemory() {
        return totalMemory - availableMemory;
    }

    public double getUsagePercentage() {
        return (double) getUsedMemory() / totalMemory * 100;
    }
}