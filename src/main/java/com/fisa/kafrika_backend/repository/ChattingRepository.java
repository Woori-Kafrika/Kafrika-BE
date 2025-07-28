package com.fisa.kafrika_backend.repository;

import com.fisa.kafrika_backend.entity.ChatMessage;
import com.fisa.kafrika_backend.entity.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);

}
