package com.dhruva.systemmonitor.services;

import com.dhruva.systemmonitor.models.DiskInfo;

import java.io.File;

public class DiskService {

    public DiskInfo getDiskInfo() {

        File root = new File("/");

        long total = root.getTotalSpace();
        long free = root.getUsableSpace();

        return new DiskInfo(total, free);

    }

}