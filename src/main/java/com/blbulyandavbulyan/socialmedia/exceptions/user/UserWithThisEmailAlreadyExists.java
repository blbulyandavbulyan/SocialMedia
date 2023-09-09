package com.blbulyandavbulyan.socialmedia.exceptions.user;

import com.blbulyandavbulyan.socialmedia.exceptions.SocialMediaException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;

public class UserWithThisEmailAlreadyExists extends SocialMediaException {
    public UserWithThisEmailAlreadyExists(@NotNull @Email String email) {
        super("User with email '" + email + "' already exists!", HttpStatus.BAD_REQUEST);
    }
}
