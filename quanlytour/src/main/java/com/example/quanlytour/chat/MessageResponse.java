package com.example.quanlytour.chat;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        String senderUsername,
        String content,
        LocalDateTime sentAt) {

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSender().getUsername(),
                message.getContent(),
                message.getSentAt());
    }

}