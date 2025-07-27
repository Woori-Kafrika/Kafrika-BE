package com.fisa.kafrika_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserLoginRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String pw;

}
