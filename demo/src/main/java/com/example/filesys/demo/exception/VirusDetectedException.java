package com.example.filesys.demo.exception;

public class VirusDetectedException extends RuntimeException {

    private final String threatName;
    private final String fileName;

    public VirusDetectedException(String threatName, String fileName) {
        super("Virus detected in file '" + fileName + "': " + threatName);
        this.threatName = threatName;
        this.fileName = fileName;
    }

    public String getThreatName() {
        return threatName;
    }

    public String getFileName() {
        return fileName;
    }
}
