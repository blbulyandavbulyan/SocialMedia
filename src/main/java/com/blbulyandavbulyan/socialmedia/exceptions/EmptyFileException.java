package com.blbulyandavbulyan.socialmedia.exceptions;

import org.springframework.http.HttpStatus;

public class EmptyFileException extends SocialMediaException {
    public EmptyFileException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
