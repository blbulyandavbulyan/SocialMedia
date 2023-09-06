package com.blbulyandavbulyan.socialmedia.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "socialmedia.files")
@Component
public class FileConfigurationProperties {
    private HashMap<String, Set<String>> mimeTypeToValidExtension;
    private Path path;
    public Optional<Set<String>> getValidExtensionForMimeType(String mimeType) {
        return Optional.ofNullable(mimeTypeToValidExtension.get(mimeType));
    }
}
