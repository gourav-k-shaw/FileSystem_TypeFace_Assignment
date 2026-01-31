package com.example.filesys.demo.service;

import com.example.filesys.demo.dto.FileResponseDto;
import com.example.filesys.demo.dto.VirusScanResult;
import com.example.filesys.demo.entity.FileEntity;
import com.example.filesys.demo.exception.VirusScannerUnavailableException;
import com.example.filesys.demo.repository.FileRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import java.nio.file.Path;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final FileStorageService fileStorageService;
    private final VirusScanService virusScanService;
    private final FileRepository fileRepository;

    public FileService(FileStorageService fileStorageService,
            VirusScanService virusScanService,
            FileRepository fileRepository) {
        this.fileStorageService = fileStorageService;
        this.virusScanService = virusScanService;
        this.fileRepository = fileRepository;
    }

    public FileResponseDto uploadFile(MultipartFile file) {

        // 1. First, check if virus scanner is available (fail-safe)
        if (!virusScanService.isAvailable()) {
            logger.error("Virus scanner is unavailable, rejecting upload");
            throw new VirusScannerUnavailableException(
                    "Virus scanner is currently unavailable. Please try again later.");
        }

        // 2. Store file temporarily for scanning
        Path tempFilePath = fileStorageService.storeTemporaryFile(file);
        logger.info("File stored temporarily at: {}", tempFilePath);

        try {
            // 3. Scan the file for viruses
            VirusScanResult scanResult = virusScanService.scanFile(tempFilePath);
            logger.info("Virus scan completed with status: {}", scanResult.getStatus());

            // 4. Handle scan results
            if (scanResult.isClean()) {
                // File is clean - move to permanent storage
                String permanentPath = fileStorageService.moveToPermanentStorage(tempFilePath);
                logger.info("Clean file moved to permanent storage: {}", permanentPath);

                // 5. Create metadata entity and save to DB
                FileEntity entity = new FileEntity(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        permanentPath);

                FileEntity saved = fileRepository.save(entity);
                logger.info("File metadata saved to database with ID: {}", saved.getId());

                // 6. Return DTO
                return new FileResponseDto(
                        saved.getId(),
                        saved.getOriginalName(),
                        saved.getContentType(),
                        saved.getSize(),
                        saved.getUploadedAt());

            } else if (scanResult.getStatus() == VirusScanResult.ScanStatus.INFECTED) {
                // File is infected - move to quarantine
                fileStorageService.moveToQuarantine(tempFilePath);
                logger.warn("Infected file moved to quarantine. Threats: {}", scanResult.getMessage());
                throw new IllegalArgumentException("File is infected: " + scanResult.getMessage());

            } else {
                // Scan error - delete temporary file
                fileStorageService.deleteFile(tempFilePath);
                logger.error("Virus scan failed: {}", scanResult.getMessage());
                throw new RuntimeException("Virus scan failed: " + scanResult.getMessage());
            }

        } catch (Exception e) {
            // Clean up temporary file on any error
            fileStorageService.deleteFile(tempFilePath);
            logger.error("Error during file upload process", e);
            throw e;
        }
    }

    public List<FileResponseDto> getAllFiles() {
        List<FileEntity> entities = fileRepository.findAllByOrderByUploadedAtDesc();
        return entities.stream()
                .map(entity -> new FileResponseDto(
                        entity.getId(),
                        entity.getOriginalName(),
                        entity.getContentType(),
                        entity.getSize(),
                        entity.getUploadedAt()))
                .collect(Collectors.toList());
    }

    public Resource loadFileAsResource(UUID id) {

        FileEntity file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        try {
            Path path = Path.of(file.getStoragePath());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalArgumentException("File not readable");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid file path", e);
        }
    }
}
