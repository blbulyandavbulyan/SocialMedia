package com.blbulyandavbulyan.socialmedia.exceptions.publications;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.HttpStatus;

public class PublicationNotFoundException extends SocialMediaException {
    public PublicationNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
