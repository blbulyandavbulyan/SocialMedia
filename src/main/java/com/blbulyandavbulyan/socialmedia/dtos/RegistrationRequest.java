package com.blbulyandavbulyan.socialmedia.dtos;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidRawPassword;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserEmail;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;

public record RegistrationRequest (
        @ValidUserName String username,
        @ValidRawPassword String password,
        @ValidUserEmail String email
){
}
