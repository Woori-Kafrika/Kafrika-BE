package com.fisa.kafrika_backend.common.response.status;

import org.springframework.http.HttpStatus;

public interface ResponseStatus {

    int getCode();

    HttpStatus getStatus();

    String getMessage();
}