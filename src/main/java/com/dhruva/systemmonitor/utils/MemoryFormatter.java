package com.dhruva.systemmonitor.utils;

public class MemoryFormatter {

    public static String formatKB(long memoryKB) {

        double memoryMB = memoryKB / 1024.0;

        if (memoryMB >= 1024) {
            return String.format("%.2f GB", memoryMB / 1024);
        }

        return String.format("%.1f MB", memoryMB);

    }

}