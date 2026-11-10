package com.example.quanlytour.chat;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {

    boolean existsByConversationIdAndUserUsername(Long conversationId, String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ConversationMember> findForUpdateByConversationIdAndUserId(Long conversationId, Long userId);

    @EntityGraph(attributePaths = {"conversation", "conversation.lichTrinh"})
    List<ConversationMember> findByUserUsernameOrderByConversationIdAsc(String username);

    long countByConversationId(Long conversationId);

}
