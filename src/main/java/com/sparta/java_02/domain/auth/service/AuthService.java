package com.sparta.java_02.domain.auth.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.auth.dto.LoginRequest;
import com.sparta.java_02.domain.auth.dto.LoginResponse;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  @Transactional
  public LoginResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() ->
            new ServiceException(ServiceExceptionCode.NOT_FOUND_USER)
        );

    // 패스워드 검증
    return LoginResponse.builder()
        .userId(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .build();
  }

  public LoginResponse getLoginResponse(Long userId, String name, String email) {
    return LoginResponse.builder()
        .userId(userId)
        .name(name)
        .email(email)
        .build();
  }
}
