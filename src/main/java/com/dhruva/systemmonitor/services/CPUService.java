package com.dhruva.systemmonitor.services;

import com.dhruva.systemmonitor.models.CPUInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CPUService {

    private long previousTotal = 0;
    private long previousIdle = 0;

    public CPUInfo getCPUInfo() {

        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/stat"))) {

            String line = reader.readLine();

            if (line == null || !line.startsWith("cpu")) {
                return new CPUInfo(0);
            }

            String[] values = line.trim().split("\\s+");

            long user = Long.parseLong(values[1]);
            long nice = Long.parseLong(values[2]);
            long system = Long.parseLong(values[3]);
            long idle = Long.parseLong(values[4]);
            long iowait = Long.parseLong(values[5]);
            long irq = Long.parseLong(values[6]);
            long softirq = Long.parseLong(values[7]);
            long steal = values.length > 8 ? Long.parseLong(values[8]) : 0;

            long idleTime = idle + iowait;

            long totalTime =
                    user +
                            nice +
                            system +
                            idle +
                            iowait +
                            irq +
                            softirq +
                            steal;

            long totalDifference = totalTime - previousTotal;
            long idleDifference = idleTime - previousIdle;

            double cpuUsage = 0;

            if (totalDifference > 0) {
                cpuUsage = (double) (totalDifference - idleDifference)
                        / totalDifference * 100;
            }

            previousTotal = totalTime;
            previousIdle = idleTime;

            return new CPUInfo(cpuUsage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new CPUInfo(0);

    }

}