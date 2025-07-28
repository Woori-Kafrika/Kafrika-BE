package com.fisa.kafrika_backend.service;

import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.DUPLICATE_ID;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.ID_NOT_FOUND;
import static com.fisa.kafrika_backend.common.response.status.BaseExceptionResponseStatus.PASSWORD_NOT_MATCH;

import com.fisa.kafrika_backend.common.exception.CustomException;
import com.fisa.kafrika_backend.dto.PostUserLoginRequest;
import com.fisa.kafrika_backend.dto.PostUserSignupRequest;
import com.fisa.kafrika_backend.entity.User;
import com.fisa.kafrika_backend.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(PostUserSignupRequest postUserSignupRequest) {
        String loginId = postUserSignupRequest.getId();

        if(userRepository.existsByLoginId(loginId)) {
            throw new CustomException(DUPLICATE_ID);
        }

        String name = postUserSignupRequest.getName();
        String loginPw = postUserSignupRequest.getPw();
        userRepository.save(User.builder()
                .name(name)
                .loginId(loginId)
                .loginPw(loginPw)
                .build());
    }

    @Transactional(readOnly = true)
    public long login(PostUserLoginRequest postUserLoginRequest) {
        String loginId = postUserLoginRequest.getId();
        Optional<User> user = userRepository.findByLoginId(loginId);

        if(user.isEmpty()) {
            throw new CustomException(ID_NOT_FOUND);
        }

        String loginPw = postUserLoginRequest.getPw();
        if (!user.get().getLoginPw().equals(loginPw)) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        return user.get().getId();
    }
}
