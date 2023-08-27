package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.configs.FileConfigurationProperties;
import com.blbulyandavbulyan.socialmedia.entites.File;
import com.blbulyandavbulyan.socialmedia.entites.User;
import com.blbulyandavbulyan.socialmedia.repositories.FileRepository;
import com.blbulyandavbulyan.socialmedia.repositories.UserRepository;
import com.blbulyandavbulyan.socialmedia.utils.ExtensionResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileService {
    private FileConfigurationProperties fileConfigurationProperties;
    private FileRepository fileRepository;
    private UserRepository userRepository;
    private ExtensionResolver extensionResolver;
    @Transactional
    public UUID save(MultipartFile multipartFile, String publisherName) {
        User uploader = userRepository.findById(publisherName).orElseThrow();
        if(multipartFile.isEmpty())
            throw new RuntimeException();
        if(!fileConfigurationProperties.isValidMimeType(multipartFile.getContentType()))
            throw new RuntimeException();
        String fileExtension = extensionResolver.getFileExtension(multipartFile.getOriginalFilename()).orElseThrow();
        if(!fileConfigurationProperties.isValidExtension(fileExtension))
            throw new RuntimeException();
        File file = new File(UUID.randomUUID(), uploader, multipartFile.getOriginalFilename(), fileExtension, multipartFile.getContentType());
        fileRepository.save(file);
        try {
            Files.copy(multipartFile.getInputStream(), fileConfigurationProperties.getPath().resolve(file.getSavedFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
            return file.getSavedFileName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
