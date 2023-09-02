package com.blbulyandavbulyan.socialmedia.dtos.messages;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class MessageResponse {
    private Long id;
    private String text;
    private boolean read;
    private Instant sendingDate;
}
