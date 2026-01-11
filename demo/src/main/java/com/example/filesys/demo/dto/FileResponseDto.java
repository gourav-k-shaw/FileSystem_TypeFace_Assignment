package com.example.filesys.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class FileResponseDto {

    private UUID id;
    private String originalName;
    private String contentType;
    private long size;
    private LocalDateTime uploadedAt;

    public FileResponseDto(UUID id,
            String originalName,
            String contentType,
            long size,
            LocalDateTime uploadedAt) {
        this.id = id;
        this.originalName = originalName;
        this.contentType = contentType;
        this.size = size;
        this.uploadedAt = uploadedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
