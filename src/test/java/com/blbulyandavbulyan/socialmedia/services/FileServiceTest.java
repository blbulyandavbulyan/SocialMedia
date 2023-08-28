package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.configs.FileConfigurationProperties;
import com.blbulyandavbulyan.socialmedia.entites.File;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.files.EmptyFileException;
import com.blbulyandavbulyan.socialmedia.exceptions.files.FileNotFoundException;
import com.blbulyandavbulyan.socialmedia.exceptions.files.UploadedFileHasInvalidExtensionException;
import com.blbulyandavbulyan.socialmedia.exceptions.files.UploadedFileHasNotAllowedMimeTypeException;
import com.blbulyandavbulyan.socialmedia.repositories.FileRepository;
import com.blbulyandavbulyan.socialmedia.utils.ExtensionResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
    private Path path = Files.createTempDirectory("socialMediaTmpDir");

    FileServiceTest() throws IOException {
    }

    @Test
    @DisplayName("save valid file")
    public void saveFileWithValidExtensionAndType() throws IOException {
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
        assertThrows(EmptyFileException.class, () -> fileService.save(mockMultipartFile, publisherName));
        Mockito.verify(fileRepository, Mockito.never()).save(any());
        Mockito.verify(userService, Mockito.only()).findByUserName(publisherName);
        Mockito.verify(fileConfigurationProperties, Mockito.never()).getPath();
    }

    @Test
    @DisplayName("save file with invalid mime type")
    public void saveFileWithInvalidMimeType() {
        String mimeType = "image/jpeg";
        String publisherName = "testuser";
        Mockito.when(userService.findByUserName(publisherName)).thenReturn(Optional.of(new User()));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.img", mimeType, new byte[]{2, 3, 4, 5, 6});
        assertThrows(UploadedFileHasNotAllowedMimeTypeException.class, () -> fileService.save(mockMultipartFile, publisherName));
        Mockito.verify(fileConfigurationProperties, Mockito.times(1)).isValidMimeType(mimeType);
        Mockito.verify(fileConfigurationProperties, Mockito.never()).getPath();
    }

    @Test
    @DisplayName("save file with invalid extension")
    public void saveFileWithInvalidExtension() {
        String mimeType = "image/jpeg";
        String publisherName = "testuser";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.jpg", mimeType, new byte[]{2, 3, 4, 5, 6});
        Mockito.when(userService.findByUserName(publisherName)).thenReturn(Optional.of(new User()));
        Mockito.when(fileConfigurationProperties.isValidExtension(".jpg")).thenReturn(false);
        Mockito.when(fileConfigurationProperties.isValidMimeType(mimeType)).thenReturn(true);
        Mockito.when(extensionResolver.getFileExtension(mockMultipartFile.getOriginalFilename())).thenReturn(Optional.of(".jpg"));
        assertThrows(UploadedFileHasInvalidExtensionException.class, () -> fileService.save(mockMultipartFile, publisherName));
        Mockito.verify(fileRepository, Mockito.never()).save(any());
        Mockito.verify(fileConfigurationProperties, Mockito.never()).getPath();
    }

    @Test
    @DisplayName("find all by id")
    public void findAllById() {
        List<File> expected = new ArrayList<>();
        List<UUID> filesUUIDs = new ArrayList<>();
        Mockito.when(fileRepository.findAllById(filesUUIDs)).thenReturn(expected);
        var actual = fileService.findAllById(filesUUIDs);
        Mockito.verify(fileRepository, Mockito.only()).findAllById(filesUUIDs);
        assertSame(expected, actual);
    }

    @Test
    @DisplayName("get file when it exists in db and in file system")
    public void getFileWhenExistsInDBAndInFileSystem() throws IOException {
        String fileName = "reallycoolimage.jpg";
        String fileExtension = ".jpg";
        String contentType = "image/jpeg";
        UUID savedFileName = UUID.randomUUID();
        com.blbulyandavbulyan.socialmedia.entites.File file = new File(savedFileName, new User(), fileName, fileExtension, contentType);
        Mockito.when(fileRepository.findById(savedFileName)).thenReturn(Optional.of(file));
        Mockito.when(fileConfigurationProperties.getPath()).thenReturn(path);
        Path expectedFilePath = path.resolve(savedFileName.toString());
        try(FileOutputStream fileOutputStream = new FileOutputStream(expectedFilePath.toString())){
            fileOutputStream.write(232);
            fileOutputStream.write(23234);
        }
        FileService.FoundFile foundFile = fileService.getFile(savedFileName);

        assertEquals(fileName, foundFile.fileName());
        assertEquals(contentType, foundFile.contentType());
        Resource resource = foundFile.resource();
        assertNotNull(resource);
        assertEquals(savedFileName.toString(), resource.getFilename());
        assertEquals(resource.getFile().toPath(), expectedFilePath);
    }
    @Test
    @DisplayName("get file when it doesn't exist in DB")
    public void getFileWhenItDoesNotExistInDB(){
        Mockito.when(fileConfigurationProperties.getPath()).thenReturn(path);
        UUID uuid = UUID.randomUUID();
        Mockito.when(fileRepository.findById(any())).thenReturn(Optional.empty());
        var actualException = assertThrows(FileNotFoundException.class, ()->fileService.getFile(uuid));
        assertEquals(HttpStatus.BAD_REQUEST, actualException.getHttpStatus());
        Mockito.verify(fileRepository, Mockito.only()).findById(uuid);
    }
}
