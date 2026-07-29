package com.dhruva.systemmonitor.services;

import com.dhruva.systemmonitor.models.NetworkInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NetworkService {

    private long previousRx = 0;
    private long previousTx = 0;

    public NetworkInfo getNetworkInfo() {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader("/proc/net/dev"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (!line.contains(":"))
                    continue;

                String interfaceName = line.split(":")[0].trim();

                // Ignore loopback interface
                if (interfaceName.equals("lo"))
                    continue;

                String[] values = line.split(":")[1].trim().split("\\s+");

                long rx = Long.parseLong(values[0]);
                long tx = Long.parseLong(values[8]);

// Skip interfaces with no traffic
                if (rx == 0 && tx == 0)
                    continue;

                // First reading: initialize previous values
                if (previousRx == 0 && previousTx == 0) {
                    previousRx = rx;
                    previousTx = tx;
                    return new NetworkInfo(0, 0);
                }

                double download = (rx - previousRx) / 1024.0;
                double upload = (tx - previousTx) / 1024.0;

                previousRx = rx;
                previousTx = tx;

                return new NetworkInfo(download, upload);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new NetworkInfo(0, 0);
    }

}