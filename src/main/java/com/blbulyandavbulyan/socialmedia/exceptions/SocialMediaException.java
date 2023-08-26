package com.blbulyandavbulyan.socialmedia.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class SocialMediaException extends RuntimeException{
    private final HttpStatus httpStatus;

    public SocialMediaException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
