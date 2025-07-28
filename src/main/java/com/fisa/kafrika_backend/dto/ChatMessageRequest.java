package com.fisa.kafrika_backend.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequest {

    private Long userId;

    private String message;
}
