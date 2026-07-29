package com.dhruva.systemmonitor.models;

public class SystemInfo {

    private final String hostname;
    private final String operatingSystem;
    private final String kernel;
    private final String architecture;
    private final String cpu;
    private final int cpuCores;
    private final String memory;
    private final String gpu;
    private final String bootTime;
    private final String uptime;


    public SystemInfo(
            String hostname,
            String operatingSystem,
            String kernel,
            String architecture,
            String cpu,
            int cpuCores,
            String memory,
            String gpu,
            String bootTime,
            String uptime) {

        this.hostname = hostname;
        this.operatingSystem = operatingSystem;
        this.kernel = kernel;
        this.architecture = architecture;
        this.cpu = cpu;
        this.cpuCores = cpuCores;
        this.memory = memory;
        this.gpu = gpu;
        this.bootTime = bootTime;
        this.uptime = uptime;

    }

    public String getHostname() { return hostname; }

    public String getOperatingSystem() { return operatingSystem; }

    public String getKernel() { return kernel; }

    public String getArchitecture() { return architecture; }

    public String getCpu() { return cpu; }

    public int getCpuCores() { return cpuCores; }

    public String getMemory() { return memory; }

    public String getGpu() { return gpu; }

    public String getBootTime() { return bootTime; }

    public String getUptime() { return uptime; }

}