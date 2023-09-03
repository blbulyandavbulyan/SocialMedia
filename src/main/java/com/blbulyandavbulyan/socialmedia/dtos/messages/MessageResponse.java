package com.blbulyandavbulyan.socialmedia.dtos.messages;

public interface MessageResponse {
    Long getId();

    String getText();

    boolean isRead();

    java.time.Instant getSendingDate();
}
