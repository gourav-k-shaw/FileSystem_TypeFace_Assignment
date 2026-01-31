package com.example.filesys.demo.service;

import com.example.filesys.demo.dto.VirusScanResult;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VirusScanService {

    private static final Logger logger = LoggerFactory.getLogger(VirusScanService.class);

    private final ClamavClient clamavClient;
    private final boolean enabled;

    public VirusScanService(
            @Value("${clamav.host}") String host,
            @Value("${clamav.port}") int port,
            @Value("${clamav.timeout}") int timeout,
            @Value("${clamav.enabled}") boolean enabled) {
        this.enabled = enabled;
        this.clamavClient = new ClamavClient(host, port);
        logger.info("VirusScanService initialized - Host: {}, Port: {}, Enabled: {}", host, port, enabled);
    }

    /**
     * Scans a file for viruses using ClamAV
     * 
     * @param filePath Path to the file to scan
     * @return VirusScanResult containing scan status and details
     */
    public VirusScanResult scanFile(Path filePath) {
        if (!enabled) {
            logger.warn("Virus scanning is disabled in configuration");
            return VirusScanResult.error("Virus scanning is disabled");
        }

        try {
            logger.info("Scanning file: {}", filePath);

            // Check if file exists
            if (!Files.exists(filePath)) {
                logger.error("File not found: {}", filePath);
                return VirusScanResult.error("File not found");
            }

            // Scan the file
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                ScanResult scanResult = clamavClient.scan(inputStream);

                if (scanResult == null) {
                    logger.error("Scan result is null for file: {}", filePath);
                    return VirusScanResult.error("Scan failed - no result");
                }

                // Handle the sealed class ScanResult
                if (scanResult instanceof ScanResult.OK) {
                    logger.info("File is clean: {}", filePath);
                    return VirusScanResult.clean();
                } else if (scanResult instanceof ScanResult.VirusFound) {
                    ScanResult.VirusFound virusFound = (ScanResult.VirusFound) scanResult;
                    // Extract virus names from the map
                    String threats = virusFound.getFoundViruses().values().stream()
                            .flatMap(java.util.Collection::stream)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("Unknown threat");
                    logger.warn("Virus detected in file: {} - Threats: {}", filePath, threats);
                    return VirusScanResult.infected(threats);
                } else {
                    logger.error("Unknown scan result type for file: {}", filePath);
                    return VirusScanResult.error("Unknown scan result");
                }
            }

        } catch (IOException e) {
            logger.error("Error scanning file: {} - {}", filePath, e.getMessage(), e);
            return VirusScanResult.error("Scan failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during virus scan: {} - {}", filePath, e.getMessage(), e);
            return VirusScanResult.error("Scan error: " + e.getMessage());
        }
    }

    /**
     * Checks if ClamAV daemon is available
     * 
     * @return true if ClamAV is reachable, false otherwise
     */
    public boolean isAvailable() {
        if (!enabled) {
            return false;
        }

        try {
            clamavClient.ping();
            return true;
        } catch (Exception e) {
            logger.error("ClamAV is not available: {}", e.getMessage());
            return false;
        }
    }
}
