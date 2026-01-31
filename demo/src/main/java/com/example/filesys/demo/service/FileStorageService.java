package com.example.filesys.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("txt", "jpg", "png", "json", "jpeg", "pdf", "doc",
            "docx");

    private final Path uploadRootPermanent;
    private final Path uploadRootTemp;
    private final Path uploadRootQuarantine;

    public FileStorageService(
            @Value("${file.upload-dir}") String uploadDir,
            @Value("${file.upload-dir-temp}") String uploadDirTemp,
            @Value("${file.upload-dir-quarantine}") String uploadDirQuarantine) {
        try {
            this.uploadRootPermanent = Paths.get(uploadDir).toAbsolutePath().normalize();
            this.uploadRootTemp = Paths.get(uploadDirTemp).toAbsolutePath().normalize();
            this.uploadRootQuarantine = Paths.get(uploadDirQuarantine).toAbsolutePath().normalize();

            // Create all directories
            Files.createDirectories(this.uploadRootPermanent);
            Files.createDirectories(this.uploadRootTemp);
            Files.createDirectories(this.uploadRootQuarantine);

            logger.info("File storage initialized - Permanent: {}, Temp: {}, Quarantine: {}",
                    uploadRootPermanent, uploadRootTemp, uploadRootQuarantine);
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize upload directories", e);
        }
    }

    /**
     * Stores file in temporary location for virus scanning
     */
    public Path storeTemporaryFile(MultipartFile file) {
        validateFile(file);

        try {
            String extension = getFileExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + "." + extension;
            Path targetLocation = uploadRootTemp.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            logger.info("Stored temporary file: {}", targetLocation);
            return targetLocation;

        } catch (IOException ex) {
            throw new RuntimeException("Failed to store temporary file", ex);
        }
    }

    /**
     * Moves file from temporary to permanent storage after successful virus scan
     */
    public String moveToPermanentStorage(Path tempPath) {
        try {
            String fileName = tempPath.getFileName().toString();
            Path targetLocation = uploadRootPermanent.resolve(fileName);

            Files.move(tempPath, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            logger.info("Moved file to permanent storage: {}", targetLocation);
            return targetLocation.toString();

        } catch (IOException ex) {
            throw new RuntimeException("Failed to move file to permanent storage", ex);
        }
    }

    /**
     * Moves infected file to quarantine
     */
    public void moveToQuarantine(Path filePath) {
        try {
            String fileName = filePath.getFileName().toString();
            Path targetLocation = uploadRootQuarantine.resolve(fileName);

            Files.move(filePath, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            logger.warn("Moved infected file to quarantine: {}", targetLocation);

        } catch (IOException ex) {
            logger.error("Failed to move file to quarantine: {}", filePath, ex);
            // Try to delete the file as fallback
            deleteFile(filePath);
        }
    }

    /**
     * Deletes a file safely
     */
    public void deleteFile(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
            logger.info("Deleted file: {}", filePath);
        } catch (IOException ex) {
            logger.error("Failed to delete file: {}", filePath, ex);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String extension = getFileExtension(filename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("File type not supported: " + extension);
        }
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
