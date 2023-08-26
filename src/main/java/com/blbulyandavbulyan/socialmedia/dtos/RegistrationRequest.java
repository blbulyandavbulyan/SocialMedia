package com.blbulyandavbulyan.socialmedia.dtos;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidPassword;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserEmail;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;

public record RegistrationRequest (
        @ValidUserName String username,
        @ValidPassword String password,
        @ValidUserEmail String email
){
}
