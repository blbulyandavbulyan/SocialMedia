package com.blbulyandavbulyan.socialmedia.services.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileStorage {
    void save(MultipartFile multipartFile, UUID fileUUID);

    Resource getFile(UUID fileUIID);
}
