package com.fisa.kafrika_backend.controller;

import com.fisa.kafrika_backend.dto.ChatMessageRequest;
import com.fisa.kafrika_backend.dto.ChatMessageResponse;
import com.fisa.kafrika_backend.service.ChatService;
import java.io.IOException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅 참여
    @SubscribeMapping("/chat")
    public ArrayList<ChatMessageResponse> subscribeChatRoom () {
        return chatService.readChatMessageLog();
    }

    // 채팅 전송
    @MessageMapping("/chat")
    @SendTo("/sub/chat")
    public ChatMessageResponse sendChatMessage (@Payload ChatMessageRequest chatMessageRequest) {
        return chatService.sendChatMessage(chatMessageRequest);
    }

}
