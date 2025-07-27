package com.fisa.kafrika_backend.common.exception.handler;

import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.HTTP_MESSAGE_NOT_READABLE;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.METHOD_NOT_ALLOWED;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.SERVER_ERROR;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.URL_NOT_FOUND;

import com.fisa.kafrika_backend.common.exception.BadRequestException;
import com.fisa.kafrika_backend.common.exception.InternalServerErrorException;
import com.fisa.kafrika_backend.common.response.BaseErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class, NoHandlerFoundException.class, TypeMismatchException.class})
    public BaseErrorResponse handle_BadRequest(Exception e) {
        log.error("[handle_BadRequest]", e);
        return new BaseErrorResponse(URL_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseErrorResponse handle_HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[handle_HttpRequestMethodNotSupportedException]", e);
        return new BaseErrorResponse(METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseErrorResponse handle_ConstraintViolationException(ConstraintViolationException e) {
        log.error("[handle_ConstraintViolationException]", e);
        return new BaseErrorResponse(BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public BaseErrorResponse handle_InternalServerError(InternalServerErrorException e) {
        log.error("[handle_InternalServerError]", e);
        return new BaseErrorResponse(e.getExceptionStatus());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public BaseErrorResponse handle_RuntimeException(Exception e) {
        log.error("[handle_RuntimeException]", e);
        return new BaseErrorResponse(SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMessageConversionException.class})
    public BaseErrorResponse HttpNotReadableException(Exception e, HttpServletRequest request) {
        log.error("[handle_RuntimeException]", e);

        String requestBody = getRequestBody(request);


        return new BaseErrorResponse(HTTP_MESSAGE_NOT_READABLE,HTTP_MESSAGE_NOT_READABLE.getMessage()+" : request body content : "+requestBody + " :content end");
    }

    private String getRequestBody(HttpServletRequest request){
        String requestBody = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    requestBody = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (IOException e) {
                    // 예외 처리
                    return "IO exception in parsing request body";
                }
            }
        }


        return requestBody.toString();

    }

}
