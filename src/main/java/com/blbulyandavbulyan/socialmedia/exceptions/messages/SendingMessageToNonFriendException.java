package com.blbulyandavbulyan.socialmedia.exceptions.messages;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class SendingMessageToNonFriendException extends SocialMediaException {
    public SendingMessageToNonFriendException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
