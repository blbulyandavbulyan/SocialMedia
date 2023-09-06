package com.blbulyandavbulyan.socialmedia.exceptions.publications;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class YouAreNotOwnThisPublicationException extends SocialMediaException {
    public YouAreNotOwnThisPublicationException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
