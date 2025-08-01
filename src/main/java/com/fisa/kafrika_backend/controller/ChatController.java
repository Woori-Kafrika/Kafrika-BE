package com.fisa.kafrika_backend.controller;

import com.fisa.kafrika_backend.common.response.BaseResponse;
import com.fisa.kafrika_backend.dto.ChatMessageRequest;
import com.fisa.kafrika_backend.dto.ChatMessageResponse;
import com.fisa.kafrika_backend.service.ChatService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @RestController
    @RequiredArgsConstructor
    public static class ChatRestController {
        private final ChatService chatService;

        @GetMapping("/chat/log")
        public BaseResponse<List<ChatMessageResponse>> getChatLog() {
            return new BaseResponse<>(chatService.readChatMessageLog());
        }
    }

    // 채팅 전송: 카프카 미사용
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatMessageResponse sendChatMessage (@Payload ChatMessageRequest chatMessageRequest) {
        return chatService.sendChatMessage(chatMessageRequest);
    }

    // 채팅 전송: 카프카 사용
    @MessageMapping("/kafka-chat")
    public void sendKafkaChatMessage(@Payload ChatMessageRequest chatMessageRequest) {
        System.out.println("카프카 컨트롤러 시작");
        chatService.sendKafkaChatMessage(chatMessageRequest);
    }

}
