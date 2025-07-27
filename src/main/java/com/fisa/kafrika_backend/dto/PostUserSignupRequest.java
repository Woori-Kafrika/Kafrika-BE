package com.fisa.kafrika_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserSignupRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String id;

    @NotBlank
    private String pw;
}
