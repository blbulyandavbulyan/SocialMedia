package com.blbulyandavbulyan.socialmedia.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationRequest (
        @Size(min = 1)
        @Size(max = 50)
        @NotBlank String username,
        @Size(min = 10)
        @NotBlank String password,
        @NotNull
        @Email String email
){
}
