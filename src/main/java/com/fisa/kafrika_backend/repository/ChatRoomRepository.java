package com.fisa.kafrika_backend.repository;

import com.fisa.kafrika_backend.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByName(String name);

}
