package com.dhruva.systemmonitor.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProcessMemoryService {

    public long getMemoryUsageKB(long pid) {

        String statusFile = "/proc/" + pid + "/status";

        try (BufferedReader reader = new BufferedReader(new FileReader(statusFile))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.startsWith("VmRSS:")) {

                    String value = line.replaceAll("\\D+", "");

                    if (!value.isEmpty()) {
                        return Long.parseLong(value);
                    }

                }

            }

        } catch (IOException ignored) {

        }

        return 0;

    }

}