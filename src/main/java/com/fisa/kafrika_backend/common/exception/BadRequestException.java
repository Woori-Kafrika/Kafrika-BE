package com.fisa.kafrika_backend.common.exception;

import com.fisa.kafrika_backend.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ResponseStatus exceptionStatus;

    public BadRequestException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.exceptionStatus = responseStatus;
    }
}
