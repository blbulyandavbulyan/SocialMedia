package com.blbulyandavbulyan.socialmedia.dtos.errors;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
public class ValidationError extends ApplicationError{
    private final Map<String, List<String>> fieldErrors;
    public ValidationError(HttpStatusCode httpStatusCode, String message, Instant instant, Map<String, List<String>> fieldErrors) {
        super(httpStatusCode, message, instant);
        this.fieldErrors = fieldErrors;
    }
}
