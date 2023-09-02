package com.blbulyandavbulyan.socialmedia.dtos.messages;

import com.blbulyandavbulyan.socialmedia.annotations.validation.ValidUserName;
import jakarta.validation.constraints.NotBlank;

public record CreateMessageRequest(@ValidUserName String receiverName, @NotBlank String text) {
}
