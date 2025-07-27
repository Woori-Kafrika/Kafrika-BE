package com.fisa.kafrika_backend.common.exception.handler;

import com.fisa.kafrika_backend.common.exception.CustomException;
import com.fisa.kafrika_backend.common.response.BaseErrorResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseErrorResponse> handleCustomException(CustomException e) {
        log.error("[handleCustomException]", e);
        BaseErrorResponse errorResponse = new BaseErrorResponse(e.getExceptionStatus(), e.getMessage());
        return ResponseEntity.status(e.getExceptionStatus().getStatus()).body(errorResponse);
    }

}
