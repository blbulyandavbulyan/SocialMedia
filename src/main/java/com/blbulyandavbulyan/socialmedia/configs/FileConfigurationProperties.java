package com.blbulyandavbulyan.socialmedia.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "socialmedia.files")
@Component
public class FileConfigurationProperties {
    private Set<String> validExtensions;
    private Set<String> validMimeTypes;
    private Path path;
    public boolean isValidExtension(String extension){
        return validExtensions.contains(extension);
    }
    public boolean isValidMimeType(String mimeType){
        return validMimeTypes.contains(mimeType);
    }
}
