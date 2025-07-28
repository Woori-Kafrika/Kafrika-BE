package com.fisa.kafrika_backend.common.config;

import com.fisa.kafrika_backend.entity.ChatRoom;
import com.fisa.kafrika_backend.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatRoomInitializer implements CommandLineRunner {

    @Value("${chat.room.default-name}")
    private String defaultRoomName;

    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (chatRoomRepository.findByName(defaultRoomName).isEmpty()) {
            chatRoomRepository.save(ChatRoom.builder()
                    .name(defaultRoomName)
                    .build());
            System.out.println("디폴트 채팅방 생성 완료");
        }
    }

}
