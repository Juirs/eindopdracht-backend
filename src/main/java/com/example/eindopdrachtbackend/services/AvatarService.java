package com.example.eindopdrachtbackend.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AvatarService {

    @Value("${app.avatar.upload-dir}")
    private String uploadDir;

    @Value("${app.avatar.max-size}")
    private long maxFileSize;

    @Value("${app.avatar.allowed-types}")
    private String allowedTypes;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

    public String uploadAvatar(MultipartFile file, String username) throws IOException {
        validateFile(file);

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename to avoid conflicts
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String uniqueFilename = username + "_" + UUID.randomUUID().toString() + "." + extension;

        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return relative path for storage in database
        return uploadDir + "/" + uniqueFilename;
    }

    public void deleteAvatar(String avatarPath) {
        if (avatarPath == null || avatarPath.isEmpty()) {
            return;
        }

        try {
            Path filePath = Paths.get(avatarPath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            // Log error but don't throw - avatar deletion shouldn't break profile updates
            System.err.println("Failed to delete avatar file: " + avatarPath + " - " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Avatar file cannot be empty");
        }

        // Check file size
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("Avatar file size cannot exceed " + (maxFileSize / 1024 / 1024) + "MB");
        }

        // Check file type
        String contentType = file.getContentType();
        List<String> allowedTypesList = Arrays.asList(allowedTypes.split(","));
        if (contentType == null || !allowedTypesList.contains(contentType)) {
            throw new IllegalArgumentException("Avatar file must be an image (JPEG, PNG, GIF, or WebP)");
        }

        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Avatar file must have a valid filename");
        }

        String extension = FilenameUtils.getExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Avatar file must have a valid image extension");
        }
    }

    // Get the full file path for serving files
    public Path getAvatarPath(String relativePath) {
        return Paths.get(relativePath);
    }
}
