package com.blbulyandavbulyan.socialmedia.exceptions.files;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class UploadedFileHasNotAllowedMimeTypeException extends SocialMediaException {
    public UploadedFileHasNotAllowedMimeTypeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
