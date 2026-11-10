package com.example.quanlytour.chat;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByLichTrinhId(Long lichTrinhId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Conversation> findForUpdateByLichTrinhId(Long lichTrinhId);

}
