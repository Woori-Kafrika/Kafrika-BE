package com.fisa.kafrika_backend.common.config;

import com.fisa.kafrika_backend.entity.ChatRoom;
import com.fisa.kafrika_backend.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatRoomInitializer implements CommandLineRunner {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (chatRoomRepository.findByName("default-chatroom").isEmpty()) {
            chatRoomRepository.save(ChatRoom.builder()
                    .name("default-chatroom")
                    .build());
            System.out.println("디폴트 채팅방 생성 완료");
        }
    }

}
