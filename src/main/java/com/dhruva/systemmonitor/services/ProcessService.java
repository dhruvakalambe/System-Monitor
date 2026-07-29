package com.dhruva.systemmonitor.services;

import com.dhruva.systemmonitor.models.ProcessInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessService {

    private final ProcessMemoryService memoryService;
    private final ProcessCPUService cpuService;

    public ProcessService(ProcessMemoryService memoryService,
                          ProcessCPUService cpuService) {

        this.memoryService = memoryService;
        this.cpuService = cpuService;
    }

    public List<ProcessInfo> getProcesses() {

        List<ProcessInfo> processes = new ArrayList<>();

        File proc = new File("/proc");

        File[] files = proc.listFiles();

        if (files == null)
            return processes;

        cpuService.beginRefresh();

        for (File file : files) {

            if (!file.isDirectory())
                continue;

            if (!file.getName().matches("\\d+"))
                continue;

            long pid = Long.parseLong(file.getName());

            File status = new File(file, "status");

            if (!status.exists())
                continue;

            try (BufferedReader reader =
                         new BufferedReader(new FileReader(status))) {

                String name = "";

                String line;

                while ((line = reader.readLine()) != null) {

                    if (line.startsWith("Name:")) {
                        name = line.substring(5).trim();
                    }

                }

// Create the ProcessInfo AFTER reading the whole file
                long memoryKB = memoryService.getMemoryUsageKB(pid);

// Temporary test
                double cpuUsage = cpuService.getCpuUsage(pid);

                processes.add(
                        new ProcessInfo(
                                pid,
                                name,
                                memoryKB,
                                cpuUsage
                        )
                );

            } catch (IOException ignored) {

            }

        }

        cpuService.endRefresh();
        return processes;

    }

    public boolean terminateProcess(long pid) {

        return ProcessHandle.of(pid)
                .map(ProcessHandle::destroy)
                .orElse(false);

    }

    public boolean killProcess(long pid) {

        return ProcessHandle.of(pid)
                .map(ProcessHandle::destroyForcibly)
                .orElse(false);

    }
}