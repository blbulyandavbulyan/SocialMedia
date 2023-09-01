package com.blbulyandavbulyan.socialmedia.exceptions.subscriptions;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class SubscriptionNotFoundException extends SocialMediaException {
    public SubscriptionNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
