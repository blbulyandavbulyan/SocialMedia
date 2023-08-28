package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.configs.FileConfigurationProperties;
import com.blbulyandavbulyan.socialmedia.entites.File;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.EmptyFileException;
import com.blbulyandavbulyan.socialmedia.repositories.FileRepository;
import com.blbulyandavbulyan.socialmedia.utils.ExtensionResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @Mock
    private FileConfigurationProperties fileConfigurationProperties;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserService userService;
    @Mock
    private ExtensionResolver extensionResolver;
    @InjectMocks
    private FileService fileService;
    @Test
    @DisplayName("save valid file")
    public void saveFileWithValidExtensionAndType() throws IOException {
        Path path = Files.createTempDirectory("socialMediaTmpDir");
        Mockito.when(fileConfigurationProperties.getPath()).thenReturn(path);
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        String contentType = "image/jpeg";
        String originalFileName = "test.jpg";
        String publisherName = "david";
        User user = new User(publisherName, "2131", "test@gmail.com");
        Mockito.when(multipartFile.isEmpty()).thenReturn(false);
        Mockito.when(multipartFile.getContentType()).thenReturn(contentType);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        Mockito.when(multipartFile.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/testimages/test_image.jpg"));
        Mockito.when(fileConfigurationProperties.isValidMimeType(contentType)).thenReturn(true);
        Mockito.when(fileConfigurationProperties.isValidExtension(".jpg")).thenReturn(true);
        Mockito.when(userService.findByUserName(publisherName)).thenReturn(Optional.of(user));
        Mockito.when(extensionResolver.getFileExtension(originalFileName)).thenReturn(Optional.of(".jpg"));
        UUID savedFileUUID = fileService.save(multipartFile, publisherName);
        assertNotNull(savedFileUUID);
        var fileCapture = ArgumentCaptor.forClass(File.class);
        Mockito.verify(fileRepository, Mockito.times(1)).save(fileCapture.capture());
        File savedFile = fileCapture.getValue();
        assertEquals(savedFileUUID, savedFile.getSavedFileName());
        assertEquals(contentType, savedFile.getMimeType());
        assertEquals(originalFileName, savedFile.getRealFileName());
        assertSame(user, savedFile.getUploader());
        assertEquals(".jpg", savedFile.getFileExtension());
        Path filePath = path.resolve(savedFile.getSavedFileName().toString());
        Files.exists(filePath);
    }
    @Test
    @DisplayName("save empty file")
    public void saveEmptyFile() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("uploadedFile", InputStream.nullInputStream());
        String publisherName = "testuser";
        Mockito.when(userService.findByUserName(publisherName)).thenReturn(Optional.of(new User()));
        assertThrows(EmptyFileException.class, ()->fileService.save(mockMultipartFile, publisherName));
        Mockito.verify(fileRepository, Mockito.never()).save(any());
        Mockito.verify(userService, Mockito.only()).findByUserName(publisherName);
        Mockito.verify(fileConfigurationProperties, Mockito.never()).getPath();
    }
}
