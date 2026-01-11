package com.example.filesys.demo.service;

import com.example.filesys.demo.dto.FileResponseDto;
import com.example.filesys.demo.entity.FileEntity;
import com.example.filesys.demo.repository.FileRepository;

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

    private final FileStorageService fileStorageService;
    private final FileRepository fileRepository;

    public FileService(FileStorageService fileStorageService,
            FileRepository fileRepository) {
        this.fileStorageService = fileStorageService;
        this.fileRepository = fileRepository;
    }

    public FileResponseDto uploadFile(MultipartFile file) {

        // 1. Store file on disk
        String storagePath = fileStorageService.storeFile(file);

        // 2. Create metadata entity
        FileEntity entity = new FileEntity(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                storagePath);

        // 3. Save metadata to DB
        FileEntity saved = fileRepository.save(entity);

        // 4. Return DTO
        return new FileResponseDto(
                saved.getId(),
                saved.getOriginalName(),
                saved.getContentType(),
                saved.getSize(),
                saved.getUploadedAt());
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
