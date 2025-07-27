package com.fisa.kafrika_backend.common.exception;

import com.fisa.kafrika_backend.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class InternalServerErrorException extends RuntimeException{
    private final ResponseStatus exceptionStatus;

    public InternalServerErrorException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
