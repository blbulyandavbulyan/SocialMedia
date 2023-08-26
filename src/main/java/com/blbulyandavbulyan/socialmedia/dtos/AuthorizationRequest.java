package com.blbulyandavbulyan.socialmedia.dtos;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidRawPassword;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;

public record AuthorizationRequest(@ValidUserName String username, @ValidRawPassword String password) {
}
