package com.blbulyandavbulyan.socialmedia.exceptions.subscriptions;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyExistsException extends SocialMediaException {
    public SubscriptionAlreadyExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
