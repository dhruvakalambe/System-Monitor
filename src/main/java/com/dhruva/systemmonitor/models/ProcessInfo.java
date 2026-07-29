package com.dhruva.systemmonitor.models;

public class ProcessInfo {

    private final long pid;
    private final String name;
    private final long memoryKB;
    private final double cpuUsage;

    public ProcessInfo(long pid,
                       String name,
                       long memoryKB,
                       double cpuUsage) {

        this.pid = pid;
        this.name = name;
        this.memoryKB = memoryKB;
        this.cpuUsage = cpuUsage;
    }

    public long getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public long getMemoryKB() {
        return memoryKB;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

}