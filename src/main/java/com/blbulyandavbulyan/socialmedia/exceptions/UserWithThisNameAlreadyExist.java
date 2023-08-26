package com.blbulyandavbulyan.socialmedia.exceptions;

import org.springframework.http.HttpStatus;

public class UserWithThisNameAlreadyExist extends SocialMediaException{
    public UserWithThisNameAlreadyExist(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
