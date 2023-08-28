package com.blbulyandavbulyan.socialmedia.exceptions.files;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class InvalidUploadedFileMimeTypeException extends SocialMediaException {
    public InvalidUploadedFileMimeTypeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
