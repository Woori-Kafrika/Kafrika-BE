package com.fisa.kafrika_backend.common.config;

import com.fisa.kafrika_backend.entity.ChatRoom;
import com.fisa.kafrika_backend.entity.User;
import com.fisa.kafrika_backend.repository.ChatRoomRepository;
import com.fisa.kafrika_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatSetInitializer implements CommandLineRunner {

    public static final String ADMIN_LOGINID = "admin";
    public static final String ADMIN_LOGINPW = "admin";
    public static final String ADMIN_NAME = "관리자";
    public static final String DEFAULT_CHATROOM = "default-chatroom";

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.findByLoginId(ADMIN_LOGINID).isEmpty()) {
            userRepository.save(User.builder()
                    .name(ADMIN_NAME)
                    .loginId(ADMIN_LOGINID)
                    .loginPw(ADMIN_LOGINPW)
                    .build());
            System.out.println("어드민 유저 생성 완료");
        }

        for (int i = 1; i <= 10_000; i++) {
            String loginId = "user" + i;
            if (userRepository.findByLoginId(loginId).isEmpty()) {
                userRepository.save(User.builder()
                        .name("user" + i)
                        .loginId(loginId)
                        .loginPw("pw" + i)
                        .build());
            }
        }
        System.out.println("1만 명 유저 생성 완료");


        if (chatRoomRepository.findByName(DEFAULT_CHATROOM).isEmpty()) {
            chatRoomRepository.save(ChatRoom.builder()
                    .name(DEFAULT_CHATROOM)
                    .build());
            System.out.println("디폴트 채팅방 생성 완료");
        }
    }

}
