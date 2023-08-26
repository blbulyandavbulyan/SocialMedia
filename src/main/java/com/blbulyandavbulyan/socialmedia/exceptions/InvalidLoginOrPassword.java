package com.blbulyandavbulyan.socialmedia.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidLoginOrPassword extends SocialMediaException{
    public InvalidLoginOrPassword(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
