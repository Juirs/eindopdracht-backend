package com.example.eindopdrachtbackend.services;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class SimpleFileService {

    private final String uploadDir = "uploads";

    public String saveFile(MultipartFile file, String subfolder) throws IOException {
        if (file.isEmpty()) return null;

        File targetDir = new File(uploadDir, subfolder);
        FileUtils.forceMkdir(targetDir);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetPath = Paths.get(targetDir.getAbsolutePath(), filename);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return subfolder + "/" + filename;
    }

    public void deleteFile(String relativePath) {
        if (relativePath == null) return;
        try {
            Path filePath = Paths.get(uploadDir, relativePath);
            Files.deleteIfExists(filePath);
        } catch (Exception ignored) {}
    }

    public Path getFilePath(String relativePath) {
        return Paths.get(uploadDir, relativePath);
    }

    public String saveAvatar(MultipartFile file) throws IOException {
        return saveFile(file, "avatars");
    }

    public String saveGameImage(MultipartFile file) throws IOException {
        return saveFile(file, "game-images");
    }

    public String saveGameFile(MultipartFile file) throws IOException {
        return saveFile(file, "game-files");
    }
}
