
package com.dhruva.systemmonitor.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProcessCPUService {

    private final Map<Long, Long> previousProcessTimes = new HashMap<>();
    private final Set<Long> seenThisRefresh = new HashSet<>();

    private long previousTotalCpuTime;
    private long currentTotalCpuTime;

    public void beginRefresh() {
        currentTotalCpuTime = getTotalCpuTime();
        seenThisRefresh.clear();
    }

    public void endRefresh() {
        previousTotalCpuTime = currentTotalCpuTime;

        // Remove entries for processes that no longer exist.
        previousProcessTimes.keySet().removeIf(
                pid -> !seenThisRefresh.contains(pid)
        );
    }

    public double getCpuUsage(long pid) {
        long currentProcessCpuTime = getProcessCpuTime(pid);

        if (currentProcessCpuTime < 0) {
            return 0.0;
        }

        seenThisRefresh.add(pid);

        Long previousProcessCpuTime = previousProcessTimes.put(
                pid, currentProcessCpuTime
        );

        long totalDelta = currentTotalCpuTime - previousTotalCpuTime;

        if (previousProcessCpuTime == null
                || previousTotalCpuTime == 0
                || totalDelta <= 0) {
            return 0.0;
        }

        long processDelta = currentProcessCpuTime - previousProcessCpuTime;

        if (processDelta < 0) {
            return 0.0;
        }

        // Normalize against total system CPU time.
        // This reports process usage as a percentage of total machine capacity.
        return (processDelta * 100.0) / totalDelta;
    }

    private long getTotalCpuTime() {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader("/proc/stat"))) {

            String line = reader.readLine();

            if (line != null && line.startsWith("cpu ")) {
                String[] values = line.trim().split("\\s+");
                long total = 0;

                for (int i = 1; i < values.length; i++) {
                    total += Long.parseLong(values[i]);
                }

                return total;
            }

        } catch (IOException | NumberFormatException ignored) {
        }

        return 0;
    }

    private long getProcessCpuTime(long pid) {
        String statFile = "/proc/" + pid + "/stat";

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(statFile))) {

            String line = reader.readLine();

            if (line == null) {
                return -1;
            }

            // The command name is enclosed in parentheses and may contain spaces.
            // Find the final ')' so the remaining fields can be indexed safely.
            int closingParen = line.lastIndexOf(')');

            if (closingParen < 0 || closingParen + 2 >= line.length()) {
                return -1;
            }

            String[] fields = line.substring(closingParen + 2).trim()
                    .split("\\s+");

            // After removing PID and comm:
            // fields[0] = state (original field 3)
            // fields[11] = utime (original field 14)
            // fields[12] = stime (original field 15)
            if (fields.length <= 12) {
                return -1;
            }

            long utime = Long.parseLong(fields[11]);
            long stime = Long.parseLong(fields[12]);

            return utime + stime;

        } catch (IOException | NumberFormatException ignored) {
            return -1;
        }
    }
}