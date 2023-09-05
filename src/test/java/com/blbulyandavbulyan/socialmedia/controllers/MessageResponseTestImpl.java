package com.blbulyandavbulyan.socialmedia.controllers;

import com.blbulyandavbulyan.socialmedia.dtos.messages.MessageResponse;
import com.blbulyandavbulyan.socialmedia.entites.Message;

import java.time.Instant;

class MessageResponseTestImpl implements MessageResponse {
    private final Message m;

    public MessageResponseTestImpl(Message m) {
        this.m = m;
    }

    @Override
    public Long getId() {
        return m.getId();
    }

    @Override
    public String getReceiverUsername() {
        return m.getReceiver().getUsername();
    }

    @Override
    public String getSenderUsername() {
        return m.getSender().getUsername();
    }

    @Override
    public String getText() {
        return m.getText();
    }

    @Override
    public boolean isRead() {
        return m.isRead();
    }

    @Override
    public Instant getSendingDate() {
        return m.getSendingDate();
    }
}
