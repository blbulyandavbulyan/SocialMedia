package com.blbulyandavbulyan.socialmedia.dtos;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidPassword;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest (
        @ValidUserName String username,
        @ValidPassword String password,
        @NotNull
        @Email String email
){
}
