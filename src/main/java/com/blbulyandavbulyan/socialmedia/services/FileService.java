package com.blbulyandavbulyan.socialmedia.services;

import com.blbulyandavbulyan.socialmedia.configs.FileConfigurationProperties;
import com.blbulyandavbulyan.socialmedia.repositories.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FileService {
    private FileConfigurationProperties fileConfigurationProperties;
    private FileRepository fileRepository;
    @Transactional
    public UUID save(MultipartFile multipartFile){
        return null;
    }
}
