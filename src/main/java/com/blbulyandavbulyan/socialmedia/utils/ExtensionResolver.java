package com.blbulyandavbulyan.socialmedia.utils;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExtensionResolver {
    public Optional<String> getFileExtension(String fileName){
        // TODO: 27.08.2023 реализовать определение расширения файла здесь
        int dotIndex = fileName.lastIndexOf(".");
        if(dotIndex != -1) {
            String extension = fileName.substring(dotIndex);
            return extension.length() > 1 ? Optional.of(extension) : Optional.empty();
        }
        return Optional.empty();
    }
}
