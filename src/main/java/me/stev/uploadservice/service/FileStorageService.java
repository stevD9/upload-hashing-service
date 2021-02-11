package me.stev.uploadservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class FileStorageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Path root = Paths.get("upload");

    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            logger.error("Couldn't create root directory, error : {}", e.getMessage());
        }
    }

    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            logger.error("Couldn't save file, error : {}", e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }
}
