package com.blbulyandavbulyan.socialmedia.exceptions.messages;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class YouAreNotReceiverOfThisMessage extends SocialMediaException {
    public YouAreNotReceiverOfThisMessage(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
