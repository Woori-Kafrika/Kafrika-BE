package com.fisa.kafrika_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChatMessage")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity{

    @Id @GeneratedValue
    @NotNull
    @Column(name = "chat_message_id")
    private long id;

    @NotNull
    @Column(name = "detail_message")
    private String detailMessage;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(String detailMessage, User user, ChatRoom chatRoom) {
        this.detailMessage = detailMessage;
        this.user = user;
        this.chatRoom = chatRoom;
    }

}
