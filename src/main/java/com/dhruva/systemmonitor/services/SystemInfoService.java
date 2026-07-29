package com.dhruva.systemmonitor.services;

import com.dhruva.systemmonitor.models.SystemInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SystemInfoService {

    public SystemInfo getSystemInfo() {

        return new SystemInfo(
                getHostname(),
                getOperatingSystem(),
                getKernel(),
                getArchitecture(),
                getCpuName(),
                getCpuCores(),
                getMemory(),
                getGpu(),
                getBootTime(),
                getUptime()
        );

    }

    //Host Name
    private String getHostname() {

        try {

            return java.net.InetAddress
                    .getLocalHost()
                    .getHostName();

        } catch (Exception e) {

            return "Unknown";

        }

    }

    //OS
    private String getOperatingSystem() {

        try (java.io.BufferedReader reader =
                     new java.io.BufferedReader(
                             new java.io.FileReader("/etc/os-release"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.startsWith("PRETTY_NAME=")) {

                    return line.substring(13, line.length() - 1);

                }

            }

        } catch (Exception ignored) {

        }

        return "Unknown";

    }

    //Kernel
    private String getKernel() {

        return System.getProperty("os.version");

    }

    //Architecture
    private String getArchitecture() {

        try {

            Process process = new ProcessBuilder("uname", "-m").start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                return reader.readLine();

            }

        } catch (Exception ignored) {
        }

        return System.getProperty("os.arch");

    }

    //CPU
    private String getCpuName() {

        try (java.io.BufferedReader reader =
                     new java.io.BufferedReader(
                             new java.io.FileReader("/proc/cpuinfo"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.startsWith("model name")) {

                    return line.split(":", 2)[1].trim();

                }

            }

        } catch (Exception ignored) {

        }

        return "Unknown";

    }

    private int getCpuCores() {

        return Runtime.getRuntime().availableProcessors();

    }

    //Memory
    private String getMemory() {

        try (java.io.BufferedReader reader =
                     new java.io.BufferedReader(
                             new java.io.FileReader("/proc/meminfo"))) {

            String line = reader.readLine();

            if (line != null) {

                long kb = Long.parseLong(
                        line.replaceAll("\\D+", "")
                );

                double gb = kb / 1024.0 / 1024.0;

                return String.format("%.1f GB", gb);

            }

        } catch (Exception ignored) {

        }

        return "Unknown";

    }

    //Uptime
    private String getUptime() {

        try (java.io.BufferedReader reader =
                     new java.io.BufferedReader(
                             new java.io.FileReader("/proc/uptime"))) {

            String line = reader.readLine();

            if (line != null) {

                long seconds = (long) Double.parseDouble(
                        line.split(" ")[0]
                );

                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long secs = seconds % 60;

                return String.format(
                        "%dh %dm %ds",
                        hours,
                        minutes,
                        secs
                );

            }

        } catch (Exception ignored) {

        }

        return "Unknown";

    }

    //GPU
    private String getGpu() {

        try {

            Process process = new ProcessBuilder("lspci").start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    String lower = line.toLowerCase();

                    if (lower.contains("vga") || lower.contains("3d")) {

                        if (line.contains("[") && line.contains("]")) {
                            return line.substring(
                                    line.indexOf('[') + 1,
                                    line.indexOf(']')
                            );
                        }

                        return line;
                    }

                }

            }

        } catch (Exception ignored) {
        }

        return "Unknown";

    }

    //Up Time
    private String getBootTime() {

        try {

            long uptimeSeconds;

            try (BufferedReader reader = new BufferedReader(
                    new FileReader("/proc/uptime"))) {

                uptimeSeconds = (long) Double.parseDouble(
                        reader.readLine().split(" ")[0]
                );

            }

            LocalDateTime bootTime =
                    LocalDateTime.now().minusSeconds(uptimeSeconds);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss");

            return bootTime.format(formatter);

        } catch (Exception e) {

            return "Unknown";

        }

    }

    //Timeline
    public String getCurrentUptime() {
        return getUptime();
    }
}