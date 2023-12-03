package com.blbulyandavbulyan.socialmedia.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@Data
@Profile("localstorage")
@ConfigurationProperties("socialmedia.localstorage")
public class LocalStorageConfiguration {
    private Path path;
}
