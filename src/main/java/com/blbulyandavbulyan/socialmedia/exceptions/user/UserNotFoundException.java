package com.blbulyandavbulyan.socialmedia.exceptions.user;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends SocialMediaException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
