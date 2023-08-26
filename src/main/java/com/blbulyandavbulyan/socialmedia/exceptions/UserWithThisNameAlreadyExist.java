package com.blbulyandavbulyan.socialmedia.exceptions;

import org.springframework.http.HttpStatus;

public class UserWithThisNameAlreadyExist extends SocialMediaException{
    public UserWithThisNameAlreadyExist(String username) {
        super("User with name '" + username + "' already exists!", HttpStatus.BAD_REQUEST);
    }
}
