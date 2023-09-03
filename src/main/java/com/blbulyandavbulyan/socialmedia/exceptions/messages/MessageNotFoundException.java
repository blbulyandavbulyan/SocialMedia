package com.blbulyandavbulyan.socialmedia.exceptions.messages;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class MessageNotFoundException extends SocialMediaException {
    public MessageNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
