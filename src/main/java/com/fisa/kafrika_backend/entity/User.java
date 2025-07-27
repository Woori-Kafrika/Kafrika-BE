package com.fisa.kafrika_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private long id;

    @Column(name = "user_name")
    private String name;

    private String loginId;

    private String loginPw;

    @Builder
    private User(Long id, String name, String loginId, String loginPw) {
        this.id = id;
        this.name = name;
        this.loginId = loginId;
        this.loginPw = loginPw;
    }
}
