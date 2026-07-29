package com.dhruva.systemmonitor.services;

import com.dhruva.systemmonitor.models.MemoryInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MemoryService {

    public MemoryInfo getMemoryInfo() {

        long total = 0;
        long available = 0;

        try {

            List<String> lines =
                    Files.readAllLines(Paths.get("/proc/meminfo"));

            for (String line : lines) {

                if (line.startsWith("MemTotal:")) {
                    total = Long.parseLong(line.replaceAll("\\D+", ""));
                }

                if (line.startsWith("MemAvailable:")) {
                    available = Long.parseLong(line.replaceAll("\\D+", ""));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new MemoryInfo(total, available);

    }

}