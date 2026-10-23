
package com.dhruva.systemmonitor.services;

import com.dhruva.systemmonitor.models.DiskInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DiskService {

    private static final Path DISKSTATS = Path.of("/proc/diskstats");
    private static final Path BLOCK_DEVICES = Path.of("/sys/block");

    private Map<String, long[]> previousCounters = new HashMap<>();
    private long previousSampleTime;

    public DiskInfo getDiskInfo() {
        File root = new File("/");

        long total = root.getTotalSpace();
        long free = root.getUsableSpace();

        Map<String, long[]> currentCounters = readDiskCounters();
        long currentTime = System.nanoTime();

        double readSpeed = 0;
        double writeSpeed = 0;

        if (previousSampleTime > 0) {
            double elapsedSeconds =
                    (currentTime - previousSampleTime) / 1_000_000_000.0;

            if (elapsedSeconds > 0) {
                long readBytes = 0;
                long writtenBytes = 0;

                for (Map.Entry<String, long[]> entry
                        : currentCounters.entrySet()) {

                    long[] current = entry.getValue();
                    long[] previous = previousCounters.get(entry.getKey());

                    if (previous != null
                            && current[0] >= previous[0]
                            && current[1] >= previous[1]) {

                        // Linux diskstats reports sectors in 512-byte units.
                        readBytes += (current[0] - previous[0]) * 512L;
                        writtenBytes += (current[1] - previous[1]) * 512L;
                    }
                }

                readSpeed = readBytes / elapsedSeconds / (1024.0 * 1024.0);
                writeSpeed = writtenBytes / elapsedSeconds / (1024.0 * 1024.0);
            }
        }

        previousCounters = currentCounters;
        previousSampleTime = currentTime;

        return new DiskInfo(total, free, readSpeed, writeSpeed);
    }

    private Map<String, long[]> readDiskCounters() {
        Map<String, long[]> counters = new HashMap<>();

        try (Stream<Path> devices = Files.list(BLOCK_DEVICES)) {
            devices.forEach(device -> {
                String name = device.getFileName().toString();

                // Exclude virtual memory and loop devices.
                if (name.startsWith("loop")
                        || name.startsWith("ram")
                        || name.startsWith("zram")) {
                    return;
                }

                try {
                    List<String> lines = Files.readAllLines(DISKSTATS);

                    for (String line : lines) {
                        String[] fields = line.trim().split("\\s+");

                        if (fields.length >= 10 && fields[2].equals(name)) {
                            long sectorsRead = Long.parseLong(fields[5]);
                            long sectorsWritten = Long.parseLong(fields[9]);

                            counters.put(
                                    name,
                                    new long[]{sectorsRead, sectorsWritten}
                            );
                            break;
                        }
                    }
                } catch (IOException | NumberFormatException e) {
                    // Skip a device if its counters cannot be read.
                }
            });
        } catch (IOException e) {
            // Keep the application running if disk statistics are unavailable.
        }

        return counters;
    }
}
