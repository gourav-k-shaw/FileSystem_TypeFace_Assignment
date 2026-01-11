package com.example.filesys.demo.repository;

import com.example.filesys.demo.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    List<FileEntity> findAllByOrderByUploadedAtDesc();

}
