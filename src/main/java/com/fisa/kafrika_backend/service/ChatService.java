package com.fisa.kafrika_backend.service;

import static com.fisa.kafrika_backend.common.config.ChatRoomInitializer.DEFAULT_CHATROOM;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.CHATROOM_NOT_FOUND;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

import com.fisa.kafrika_backend.common.exception.CustomException;
import com.fisa.kafrika_backend.dto.ChatMessageRequest;
import com.fisa.kafrika_backend.dto.ChatMessageResponse;
import com.fisa.kafrika_backend.entity.ChatMessage;
import com.fisa.kafrika_backend.entity.ChatRoom;
import com.fisa.kafrika_backend.entity.User;
import com.fisa.kafrika_backend.repository.ChatRoomRepository;
import com.fisa.kafrika_backend.repository.ChattingRepository;
import com.fisa.kafrika_backend.repository.UserRepository;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChattingRepository chattingRepository;

    @Transactional(readOnly = true)
    public ArrayList<ChatMessageResponse> readChatMessageLog() {
        ChatRoom chatRoom = chatRoomRepository.findByName(DEFAULT_CHATROOM)
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

        return chattingRepository.findByChatRoom(chatRoom).stream()
                .map(chatMessage -> ChatMessageResponse.builder()
                        .id(chatMessage.getId())
                        .senderId(chatMessage.getUser().getId())
                        .senderName(chatMessage.getUser().getName())
                        .message(chatMessage.getDetailMessage())
                        .sendAt(chatMessage.getCreatedAt())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional
    public ChatMessageResponse sendChatMessage(ChatMessageRequest chatMessageRequest) {
        ChatRoom chatRoom = chatRoomRepository.findByName(DEFAULT_CHATROOM)
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

        User user = userRepository.findById(chatMessageRequest.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatMessage saved = chattingRepository.save(
                ChatMessage.builder()
                        .chatRoom(chatRoom)
                        .user(user)
                        .detailMessage(chatMessageRequest.getMessage())
                        .build()
        );

        return ChatMessageResponse.builder()
                .id(saved.getId())
                .senderId(user.getId())
                .senderName(user.getName())
                .message(saved.getDetailMessage())
                .sendAt(saved.getCreatedAt())
                .build();
    }
}
