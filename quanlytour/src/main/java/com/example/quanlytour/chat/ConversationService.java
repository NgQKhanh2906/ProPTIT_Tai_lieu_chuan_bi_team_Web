package com.example.quanlytour.chat;

import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public Conversation joinTourGroup(LichTrinh lichTrinh, User user) {
        Conversation conversation = conversationRepository.findForUpdateByLichTrinhId(lichTrinh.getId())
                .orElseGet(() -> createTourGroup(lichTrinh));
        if (conversationMemberRepository.findForUpdateByConversationIdAndUserId(conversation.getId(), user.getId()).isEmpty()) {
            ConversationMember member = new ConversationMember();
            member.setConversation(conversation);
            member.setUser(user);
            conversationMemberRepository.save(member);
        }
        return conversation;
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> getMyConversations(String username) {
        return conversationMemberRepository.findByUserUsernameOrderByConversationIdAsc(username)
                .stream()
                .map(ConversationMember::getConversation)
                .map(conversation -> ConversationResponse.from(conversation,
                        conversationMemberRepository.countByConversationId(conversation.getId())))
                .toList();
    }

    private Conversation createTourGroup(LichTrinh lichTrinh) {
        Conversation conversation = new Conversation();
        conversation.setLichTrinh(lichTrinh);
        conversation.setName("Doan " + lichTrinh.getTour().getTourName() + " - " + lichTrinh.getDepartureDate());
        return conversationRepository.save(conversation);
    }

}
