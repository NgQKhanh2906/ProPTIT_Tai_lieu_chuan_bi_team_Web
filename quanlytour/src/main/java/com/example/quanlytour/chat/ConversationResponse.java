package com.example.quanlytour.chat;

import java.time.LocalDate;

public record ConversationResponse(
        Long id,
        String name,
        Long lichTrinhId,
        LocalDate departureDate,
        long memberCount) {

    public static ConversationResponse from(Conversation conversation, long memberCount) {
        return new ConversationResponse(
                conversation.getId(),
                conversation.getName(),
                conversation.getLichTrinh().getId(),
                conversation.getLichTrinh().getDepartureDate(),
                memberCount);
    }

}
