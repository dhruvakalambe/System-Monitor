
package com.dhruva.systemmonitor.models;

public class DiskInfo {

    private final long totalSpace;
    private final long freeSpace;
    private final long usedSpace;
    private final double usagePercentage;
    private final double readSpeedMBps;
    private final double writeSpeedMBps;

    public DiskInfo(
            long totalSpace,
            long freeSpace,
            double readSpeedMBps,
            double writeSpeedMBps
    ) {
        this.totalSpace = totalSpace;
        this.freeSpace = freeSpace;
        this.usedSpace = Math.max(0, totalSpace - freeSpace);

        if (totalSpace == 0) {
            usagePercentage = 0;
        } else {
            usagePercentage = ((double) usedSpace / totalSpace) * 100;
        }

        this.readSpeedMBps = readSpeedMBps;
        this.writeSpeedMBps = writeSpeedMBps;
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

    public double getReadSpeedMBps() {
        return readSpeedMBps;
    }

    public double getWriteSpeedMBps() {
        return writeSpeedMBps;
    }
}
