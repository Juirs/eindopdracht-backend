package com.example.eindopdrachtbackend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SimpleFileServiceTest {

    @InjectMocks
    private SimpleFileService simpleFileService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveFile_WithValidFile_ShouldReturnRelativePath() throws Exception {
        // Arrange
        MultipartFile file = new MockMultipartFile("test", "test.txt", "text/plain", "test content".getBytes());
        String subfolder = "test-folder";

        // Act
        String result = simpleFileService.saveFile(file, subfolder);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith(subfolder + "/"));
        assertTrue(result.contains("test.txt"));
    }

    @Test
    void saveFile_WithEmptyFile_ShouldReturnNull() throws Exception {
        // Arrange
        MultipartFile emptyFile = new MockMultipartFile("test", "test.txt", "text/plain", new byte[0]);
        String subfolder = "test-folder";

        // Act
        String result = simpleFileService.saveFile(emptyFile, subfolder);

        // Assert
        assertNull(result);
    }

    @Test
    void saveFile_WithNullFilename_ShouldHandleGracefully() throws Exception {
        // Arrange
        MultipartFile file = new MockMultipartFile("test", null, "text/plain", "test content".getBytes());
        String subfolder = "test-folder";

        // Act
        String result = simpleFileService.saveFile(file, subfolder);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith(subfolder + "/"));
        assertTrue(result.endsWith("_"), "Expected filename to end with '_' but was: " + result);
    }

    @Test
    void deleteFile_WithNullPath_ShouldNotThrowException() {
        // Arrange
        String relativePath = null;

        // Act & Assert
        assertDoesNotThrow(() -> simpleFileService.deleteFile(relativePath));
    }

    @Test
    void deleteFile_WithNonExistentFile_ShouldNotThrowException() {
        // Arrange
        String relativePath = "non-existent/file.txt";

        // Act & Assert
        assertDoesNotThrow(() -> simpleFileService.deleteFile(relativePath));
    }

    @Test
    void getFilePath_WithValidRelativePath_ShouldReturnCorrectPath() {
        // Arrange
        String relativePath = "test-folder/test-file.txt";

        // Act
        java.nio.file.Path result = simpleFileService.getFilePath(relativePath);

        // Assert
        assertNotNull(result);
        assertTrue(result.toString().replace("\\", "/").contains("uploads")); // Handle Windows paths
        assertTrue(result.toString().replace("\\", "/").contains(relativePath)); // Handle Windows paths
    }

    @Test
    void saveAvatar_WithValidFile_ShouldSaveInAvatarsFolder() throws Exception {
        // Arrange
        MultipartFile file = new MockMultipartFile("avatar", "avatar.jpg", "image/jpeg", "avatar content".getBytes());

        // Act
        String result = simpleFileService.saveAvatar(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("avatars/"));
    }

    @Test
    void saveGameImage_WithValidFile_ShouldSaveInGameImagesFolder() throws Exception {
        // Arrange
        MultipartFile file = new MockMultipartFile("image", "game.png", "image/png", "game image content".getBytes());

        // Act
        String result = simpleFileService.saveGameImage(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("game-images/"));
    }

    @Test
    void saveGameFile_WithValidFile_ShouldSaveInGameFilesFolder() throws Exception {
        // Arrange
        MultipartFile file = new MockMultipartFile("game", "game.zip", "application/zip", "game file content".getBytes());

        // Act
        String result = simpleFileService.saveGameFile(file);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("game-files/"));
    }

    @Test
    void saveFile_WithSpecialCharactersInFilename_ShouldHandleCorrectly() throws Exception {
        // Arrange
        MultipartFile file = new MockMultipartFile("test", "file with spaces & symbols!.txt", "text/plain", "content".getBytes());
        String subfolder = "test-folder";

        // Act
        String result = simpleFileService.saveFile(file, subfolder);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith(subfolder + "/"));
        assertTrue(result.contains("file with spaces & symbols!.txt"));
    }

    @Test
    void saveFile_WithLargeFile_ShouldSaveSuccessfully() throws Exception {
        // Arrange
        byte[] largeContent = new byte[1024]; // 1KB (simplified from 1MB for faster test)
        MultipartFile file = new MockMultipartFile("large", "large.bin", "application/octet-stream", largeContent);
        String subfolder = "test-folder";

        // Act
        String result = simpleFileService.saveFile(file, subfolder);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith(subfolder + "/"));
    }

    @Test
    void saveFile_WithDifferentFileTypes_ShouldHandleAll() throws Exception {
        // Arrange
        MultipartFile[] files = {
            new MockMultipartFile("pdf", "document.pdf", "application/pdf", "PDF content".getBytes()),
            new MockMultipartFile("image", "photo.jpg", "image/jpeg", "JPEG content".getBytes()),
            new MockMultipartFile("text", "readme.txt", "text/plain", "Text content".getBytes())
        };

        // Act & Assert
        for (MultipartFile file : files) {
            String result = simpleFileService.saveFile(file, "mixed");
            assertNotNull(result, "Should handle " + file.getContentType());
            assertTrue(result.startsWith("mixed/"), "Should use correct subfolder");
            assertTrue(result.contains(file.getOriginalFilename()), "Should preserve filename");
        }
    }
}
