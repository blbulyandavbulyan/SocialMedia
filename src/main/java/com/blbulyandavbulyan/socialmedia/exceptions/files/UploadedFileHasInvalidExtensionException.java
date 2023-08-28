package com.blbulyandavbulyan.socialmedia.exceptions.files;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class UploadedFileHasInvalidExtensionException extends SocialMediaException {
    public UploadedFileHasInvalidExtensionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
