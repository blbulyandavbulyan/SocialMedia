package com.blbulyandavbulyan.socialmedia.services.filestorage;

import com.blbulyandavbulyan.socialmedia.configs.LocalStorageConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("localstorage")
public class LocalFileStorage implements FileStorage {
    private final LocalStorageConfiguration fileConfigurationProperties;

    @Override
    public void save(MultipartFile multipartFile, UUID fileUUID) {
        try {
            Files.copy(multipartFile.getInputStream(), fileConfigurationProperties.getPath().resolve(fileUUID.toString()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource getFile(UUID fileUIID) {
        try {
            return new UrlResource(fileConfigurationProperties.getPath().resolve(fileUIID.toString()).toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
