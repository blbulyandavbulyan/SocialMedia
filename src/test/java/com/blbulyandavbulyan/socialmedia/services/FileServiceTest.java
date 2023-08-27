package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.configs.FileConfigurationProperties;
import com.blbulyandavbulyan.socialmedia.entites.File;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.FileRepository;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @Mock
    private FileConfigurationProperties fileConfigurationProperties;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private FileService fileService;
    private Path path;
    @BeforeEach
    public void init() throws IOException {
        path = Files.createTempDirectory("socialMediaTmpDir");
        Mockito.when(fileConfigurationProperties.getPath()).thenReturn(path);
    }
    @Test
    @DisplayName("save valid file")
    public void saveFileWithValidExtensionAndType() throws IOException {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        String contentType = "image/jpeg";
        String originalFileName = "test.jpg";
        String publisherName = "david";
        User user = new User(publisherName, "2131", "test@gmail.com");
        Mockito.when(multipartFile.isEmpty()).thenReturn(false);
        Mockito.when(multipartFile.getContentType()).thenReturn(contentType);
        Mockito.when(multipartFile.getSize()).thenReturn(2000L);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        Mockito.when(multipartFile.getInputStream()).thenReturn(this.getClass().getResourceAsStream("/testimages/test_image.jpg"));
        Mockito.when(fileConfigurationProperties.isValidMimeType(contentType)).thenReturn(true);
        Mockito.when(fileConfigurationProperties.isValidExtension(".jpg")).thenReturn(true);
        Mockito.when(userRepository.findById(publisherName)).thenReturn(Optional.of(user));
        UUID savedFileUUID = fileService.save(multipartFile);
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
        assertEquals(contentType, Files.probeContentType(filePath));
    }
}
