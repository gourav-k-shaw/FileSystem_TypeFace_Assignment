package com.example.filesys.demo.exception;

public class VirusScannerUnavailableException extends RuntimeException {

    public VirusScannerUnavailableException(String message) {
        super("Virus scanner is unavailable: " + message);
    }

    public VirusScannerUnavailableException(String message, Throwable cause) {
        super("Virus scanner is unavailable: " + message, cause);
    }
}
