package com.blbulyandavbulyan.socialmedia.exceptions.user;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class UserWithThisNameAlreadyExist extends SocialMediaException {
    public UserWithThisNameAlreadyExist(String username) {
        super("User with name '" + username + "' already exists!", HttpStatus.BAD_REQUEST);
    }
}
