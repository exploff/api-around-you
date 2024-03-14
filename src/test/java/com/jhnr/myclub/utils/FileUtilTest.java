package com.jhnr.myclub.utils;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUtilTest {

    @Test
    void convertMultiPartToFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("testFile", "test.txt", "text/plain", "Test content".getBytes());

        File convFile = FileUtil.convertMultiPartToFile(file);

        assertEquals("temp", convFile.getName());
        assertEquals("Test content", FileUtils.readFileToString(convFile, StandardCharsets.UTF_8));
    }

    @Test
    void getExtension() {
        String filename = "example.jpg";
        String extension = FileUtil.getExtension(filename);

        assertEquals(".jpg", extension);
    }

    @Test
    void extensionToMediaType() {
        // Test for known extensions
        assertEquals(MediaType.IMAGE_PNG, FileUtil.extensionToMediaType(".png"));
        assertEquals(MediaType.IMAGE_JPEG, FileUtil.extensionToMediaType(".jpg"));
        assertEquals(MediaType.IMAGE_JPEG, FileUtil.extensionToMediaType(".jpeg"));
        assertEquals(MediaType.IMAGE_GIF, FileUtil.extensionToMediaType(".gif"));

        // Test for unknown extension
        assertEquals(MediaType.ALL, FileUtil.extensionToMediaType(".unknown"));
    }

    @Test
    void generateFileName() {
        String userName = "john_doe";
        MockMultipartFile file = new MockMultipartFile("testFile", "test file.jpg", "image/jpeg", "Test content".getBytes());

        String fileName = FileUtil.generateFileName(userName, file);

        String expectedFileNameRegex = "john_doe/\\d+-test_file.jpg";
        assertTrue(fileName.matches(expectedFileNameRegex));
    }
}