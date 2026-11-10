package com.example.quanlytour.chat;

import com.example.quanlytour.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;

    @GetMapping("/me")
    public List<ConversationResponse> getMyConversations(@AuthenticationPrincipal User currentUser) {
        return conversationService.getMyConversations(currentUser.getUsername());
    }

    @GetMapping("/{conversationId}/messages")
    @PreAuthorize("@conversationSecurity.isMember(#conversationId, authentication)")
    public List<MessageResponse> getMessages(@PathVariable Long conversationId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        return messageService.getMessages(conversationId, page, size);
    }

    @PostMapping("/{conversationId}/messages")
    @PreAuthorize("@conversationSecurity.isMember(#conversationId, authentication)")
    public MessageResponse sendMessage(@PathVariable Long conversationId,
                                       @Valid @RequestBody SendMessageRequest request,
                                       @AuthenticationPrincipal User currentUser) {
        return messageService.sendMessage(conversationId, currentUser.getUsername(), request.content());
    }

}
