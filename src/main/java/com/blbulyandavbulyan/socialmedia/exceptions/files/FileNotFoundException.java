package com.blbulyandavbulyan.socialmedia.exceptions.files;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends SocialMediaException {
    public FileNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
