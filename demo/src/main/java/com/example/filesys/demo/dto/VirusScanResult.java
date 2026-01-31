package com.example.filesys.demo.dto;

public class VirusScanResult {

    public enum ScanStatus {
        CLEAN,
        INFECTED,
        ERROR
    }

    private final boolean isClean;
    private final String threatName;
    private final String message;
    private final ScanStatus status;

    private VirusScanResult(boolean isClean, String threatName, String message, ScanStatus status) {
        this.isClean = isClean;
        this.threatName = threatName;
        this.message = message;
        this.status = status;
    }

    public static VirusScanResult clean() {
        return new VirusScanResult(true, null, "File is clean", ScanStatus.CLEAN);
    }

    public static VirusScanResult infected(String threatName) {
        return new VirusScanResult(false, threatName,
                "Virus detected: " + threatName, ScanStatus.INFECTED);
    }

    public static VirusScanResult error(String errorMessage) {
        return new VirusScanResult(false, null, errorMessage, ScanStatus.ERROR);
    }

    public boolean isClean() {
        return isClean;
    }

    public String getThreatName() {
        return threatName;
    }

    public String getMessage() {
        return message;
    }

    public ScanStatus getStatus() {
        return status;
    }
}
