package com.fisa.kafrika_backend.service;

import static com.fisa.kafrika_backend.common.config.ChatSetInitializer.DEFAULT_CHATROOM;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.CHATROOM_NOT_FOUND;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.USER_NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.kafrika_backend.common.exception.CustomException;
import com.fisa.kafrika_backend.dto.ChatMessageRequest;
import com.fisa.kafrika_backend.entity.ChatMessage;
import com.fisa.kafrika_backend.entity.ChatRoom;
import com.fisa.kafrika_backend.entity.User;
import com.fisa.kafrika_backend.repository.ChatRoomRepository;
import com.fisa.kafrika_backend.repository.ChattingRepository;
import com.fisa.kafrika_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatKafkaListener {

    public static final String KAFKA_TOPIC = "chat-message";
    public static final String KAFKA_CONSUMER_GROUP_ID = "chat-consumer-group";

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;
    private final ChattingRepository chattingRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = KAFKA_TOPIC, groupId = KAFKA_CONSUMER_GROUP_ID)
    public void consume(String message) {
        try {
            System.out.println("consume 진입");
            ChatMessageRequest chatMessageRequest = objectMapper.readValue(message, ChatMessageRequest.class);

            ChatRoom chatRoom = chatRoomRepository.findByName(DEFAULT_CHATROOM)
                    .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

            User user = userRepository.findById(chatMessageRequest.getUserId())
                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

            chattingRepository.save(ChatMessage.builder()
                            .detailMessage(chatMessageRequest.getMessage())
                            .chatRoom(chatRoom)
                            .user(user)
                            .build());

            messagingTemplate.convertAndSend("/topic/chat", chatMessageRequest);
            log.info("브로드캐스트 완료");
        } catch (Exception e) {
            log.error("Kafka 처리 실패", e);
        }
    }
}
