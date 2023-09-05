package com.blbulyandavbulyan.socialmedia.dtos.messages;

public interface MessageResponse {
    Long getId();

    String getReceiverUsername();

    String getSenderUsername();

    String getText();

    boolean isRead();

    java.time.Instant getSendingDate();
}
