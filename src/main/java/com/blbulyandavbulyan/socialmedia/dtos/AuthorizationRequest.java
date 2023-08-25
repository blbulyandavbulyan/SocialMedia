package com.blbulyandavbulyan.socialmedia.dtos;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidPassword;
import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;

public record AuthorizationRequest(@ValidUserName String username, @ValidPassword String password) {
}
