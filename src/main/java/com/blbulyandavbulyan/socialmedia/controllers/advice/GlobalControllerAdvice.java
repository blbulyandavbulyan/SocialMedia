package com.blbulyandavbulyan.socialmedia.controllers.advice;

import com.blbulyandavbulyan.socialmedia.dtos.errors.ApplicationError;
import com.blbulyandavbulyan.socialmedia.dtos.errors.ValidationError;
import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(SocialMediaException.class)
    public ResponseEntity<ApplicationError> processSocialMediaException(SocialMediaException socialMediaException){
        return new ResponseEntity<>(new ApplicationError(socialMediaException.getHttpStatus(),
                socialMediaException.getMessage(),
                Instant.now()),
                socialMediaException.getHttpStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> processMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        Map<String, List<String>> fieldErrors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldErrors.computeIfAbsent(fieldError.getField(), (key)->new ArrayList<>()).add(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(new ValidationError(e.getStatusCode(),
                "Validation error",
                Instant.now(), fieldErrors), e.getStatusCode());
    }
}
