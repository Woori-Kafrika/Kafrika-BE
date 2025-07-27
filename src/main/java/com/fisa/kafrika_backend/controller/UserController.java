package com.fisa.kafrika_backend.controller;

import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.INVALID_USER_LOGIN;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.INVALID_USER_SIGNUP;
import static com.fisa.kafrika_backend.common.util.BindingResultUtils.getErrorMessage;

import com.fisa.kafrika_backend.common.exception.CustomException;
import com.fisa.kafrika_backend.common.response.BaseResponse;
import com.fisa.kafrika_backend.common.response.SuccessResponse;
import com.fisa.kafrika_backend.dto.PostUserLoginRequest;
import com.fisa.kafrika_backend.dto.PostUserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @PostMapping("/signup")
    public BaseResponse<SuccessResponse> signup(@Validated @RequestBody PostUserSignupRequest postUserSignupRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_USER_SIGNUP, getErrorMessage(bindingResult));
        }

        // TODO: 회원가입 로직 처리

        return new BaseResponse<>(new SuccessResponse(true));
    }

    @PostMapping("/login")
    public BaseResponse<SuccessResponse> login(@Validated @RequestBody PostUserLoginRequest postUserLoginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_USER_LOGIN, getErrorMessage(bindingResult));
        }

        // TODO: 로그인 로직 처리

        return new BaseResponse<>(new SuccessResponse(true));
    }
}
