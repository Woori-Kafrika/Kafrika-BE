package com.fisa.kafrika_backend.service;

import com.fisa.kafrika_backend.dto.ChatMessageRequest;
import com.fisa.kafrika_backend.dto.ChatMessageResponse;
import com.fisa.kafrika_backend.repository.ChatRoomRepository;
import com.fisa.kafrika_backend.repository.ChattingRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChattingRepository chattingRepository;


    public ArrayList<ChatMessageResponse> readChatMessageLog() {
        return null;
    }

    public ChatMessageResponse sendChatMessage(ChatMessageRequest chatMessageRequest) {
        return null;
    }
}
