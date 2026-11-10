package com.example.quanlytour.chat;

import com.example.quanlytour.entity.User;
import com.example.quanlytour.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    public static final int MAX_PAGE_SIZE = 50;

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(Long conversationId, int page, int size) {
        if (page < 0 || size < 1) {
            throw new IllegalStateException("page phai >= 0 va size phai >= 1");
        }
        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        return messageRepository.findByConversationIdOrderBySentAtDescIdDesc(conversationId, PageRequest.of(page, pageSize))
                .stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Transactional
    public MessageResponse sendMessage(Long conversationId, String username, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay nhom id = " + conversationId));
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay user: " + username));

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(content.trim());
        return MessageResponse.from(messageRepository.save(message));
    }

}
