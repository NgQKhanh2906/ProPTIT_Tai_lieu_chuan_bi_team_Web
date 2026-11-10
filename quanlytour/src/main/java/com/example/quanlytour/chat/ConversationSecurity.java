package com.example.quanlytour.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("conversationSecurity")
@RequiredArgsConstructor
public class ConversationSecurity {

    private final ConversationMemberRepository conversationMemberRepository;

    public boolean isMember(Long conversationId, Authentication authentication) {
        if (conversationId == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return conversationMemberRepository.existsByConversationIdAndUserUsername(conversationId, authentication.getName());
    }

}