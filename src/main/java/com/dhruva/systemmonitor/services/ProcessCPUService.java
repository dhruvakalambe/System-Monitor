package com.dhruva.systemmonitor.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProcessCPUService {

    private final Map<Long, Long> previousProcessTimes = new HashMap<>();

    private long previousTotalCpuTime = 0;
    private long currentTotalCpuTime = 0;

    public void beginRefresh() {
        currentTotalCpuTime = getTotalCpuTime();
    }

    public void endRefresh() {
        previousTotalCpuTime = currentTotalCpuTime;
    }

    public double getCpuUsage(long pid) {

        long currentProcessCpuTime = getProcessCpuTime(pid);

        // First time seeing this process
        if (!previousProcessTimes.containsKey(pid) || previousTotalCpuTime == 0) {

            previousProcessTimes.put(pid, currentProcessCpuTime);
            previousTotalCpuTime = currentTotalCpuTime;

            return 0.0;
        }

        long previousProcessCpuTime = previousProcessTimes.get(pid);

        long processDelta = currentProcessCpuTime - previousProcessCpuTime;
        long totalDelta = currentTotalCpuTime - previousTotalCpuTime;

        // Store current values for next calculation
        previousProcessTimes.put(pid, currentProcessCpuTime);
        previousTotalCpuTime = currentTotalCpuTime;

        if (totalDelta <= 0) {
            return 0.0;
        }

        return (processDelta * 100.0) / totalDelta;
    }

    private long getTotalCpuTime() {

        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/stat"))) {

            String line = reader.readLine();

            if (line != null && line.startsWith("cpu ")) {

                String[] values = line.trim().split("\\s+");

                long total = 0;

                for (int i = 1; i < values.length; i++) {
                    total += Long.parseLong(values[i]);
                }

                return total;
            }

        } catch (IOException ignored) {

        }

        return 0;

    }

    private long getProcessCpuTime(long pid) {

        String statFile = "/proc/" + pid + "/stat";

        try (BufferedReader reader = new BufferedReader(new FileReader(statFile))) {

            String line = reader.readLine();

            if (line == null)
                return 0;

            String[] values = line.split("\\s+");

            long utime = Long.parseLong(values[13]);
            long stime = Long.parseLong(values[14]);

            return utime + stime;

        } catch (IOException | NumberFormatException e) {
            return 0;
        }

    }

}