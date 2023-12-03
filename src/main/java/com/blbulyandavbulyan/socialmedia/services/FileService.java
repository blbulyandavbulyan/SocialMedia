package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.configs.FileConfigurationProperties;
import com.blbulyandavbulyan.socialmedia.entites.File;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.exceptions.files.EmptyFileException;
import com.blbulyandavbulyan.socialmedia.exceptions.files.FileNotFoundException;
import com.blbulyandavbulyan.socialmedia.exceptions.files.UploadedFileHasInvalidExtensionException;
import com.blbulyandavbulyan.socialmedia.exceptions.files.UploadedFileHasNotAllowedMimeTypeException;
import com.blbulyandavbulyan.socialmedia.repositories.FileRepository;
import com.blbulyandavbulyan.socialmedia.services.filestorage.FileStorage;
import com.blbulyandavbulyan.socialmedia.utils.ExtensionResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class FileService {
    private FileConfigurationProperties fileConfigurationProperties;
    private FileRepository fileRepository;
    private UserService userService;
    private ExtensionResolver extensionResolver;
    private FileStorage fileStorage;
    @Transactional
    public UUID save(MultipartFile multipartFile, String publisherName) {
        User uploader = userService.findByUserName(publisherName).orElseThrow();
        if(multipartFile.isEmpty())
            throw new EmptyFileException("Uploading file is empty!");
        var validExtensionsOptional = fileConfigurationProperties.getValidExtensionForMimeType(multipartFile.getContentType());
        if (validExtensionsOptional.isEmpty())
            throw new UploadedFileHasNotAllowedMimeTypeException("Uploaded file has not allowed mime type!");
        String fileExtension = extensionResolver.getFileExtension(multipartFile.getOriginalFilename()).orElseThrow();
        if (!validExtensionsOptional.get().contains(fileExtension))
            throw new UploadedFileHasInvalidExtensionException("Uploaded file has invalid extension!");
        File file = new File(UUID.randomUUID(), uploader, multipartFile.getOriginalFilename(), multipartFile.getContentType());
        fileRepository.save(file);
        fileStorage.save(multipartFile, file.getSavedFileName());
        return file.getSavedFileName();
    }

    public List<File> findAllById(List<UUID> filesUUIDs) {
        return fileRepository.findAllById(filesUUIDs);
    }

    public record FoundFile(String fileName, String contentType, Resource resource) {
    }

    public FoundFile getFile(UUID fileUIID) {
        Resource resource = fileStorage.getFile(fileUIID);
        FoundFile foundFile = fileRepository.findById(fileUIID)
                .map((file) -> new FoundFile(file.getRealFileName(), file.getMimeType(), resource))
                .orElseThrow(() -> new FileNotFoundException("File with " + fileUIID + " not found!"));
        if (!resource.exists()) {
            log.error("File with UUID '" + fileUIID + "' exists in DB but not found in file system!");
            throw new FileNotFoundException("File with " + fileUIID + " not found!");
        }
        return foundFile;
    }
}
