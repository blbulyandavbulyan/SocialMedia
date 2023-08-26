package com.blbulyandavbulyan.socialmedia.dtos.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class ApplicationError {
    private final HttpStatusCode httpStatusCode;
    private final String message;
    private final Instant timestamp;
}
