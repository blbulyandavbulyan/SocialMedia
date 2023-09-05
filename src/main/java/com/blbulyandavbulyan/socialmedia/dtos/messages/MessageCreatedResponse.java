package com.blbulyandavbulyan.socialmedia.dtos.messages;

import java.time.Instant;

public record MessageCreatedResponse(Long messageId, String receiver, Instant sendingDate) {
}
