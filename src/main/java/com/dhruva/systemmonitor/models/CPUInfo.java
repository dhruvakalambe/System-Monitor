package com.dhruva.systemmonitor.models;

public class CPUInfo {

    private double usagePercentage;

    public CPUInfo(double usagePercentage) {
        this.usagePercentage = usagePercentage;
    }

    public double getUsagePercentage() {
        return usagePercentage;
    }

    public void setUsagePercentage(double usagePercentage) {
        this.usagePercentage = usagePercentage;
    }
}