package com.fisa.kafrika_backend.common.exception;

import com.fisa.kafrika_backend.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ResponseStatus exceptionStatus;

    public CustomException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }

    public CustomException(ResponseStatus exceptionStatus, String message) {
        super(message);
        this.exceptionStatus = exceptionStatus;
    }
}
