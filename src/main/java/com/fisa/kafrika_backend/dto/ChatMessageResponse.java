package com.fisa.kafrika_backend.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {

    private String senderId;

    private String senderName;

    private String message;

    private LocalDateTime sendAt;

}
