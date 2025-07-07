package com.sparta.java_02.domain.auth.controller;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.auth.dto.LoginRequest;
import com.sparta.java_02.domain.auth.dto.LoginResponse;
import com.sparta.java_02.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ApiResponse<Void> login(HttpSession session, @Valid @RequestBody LoginRequest request) {

    LoginResponse loginResponse = authService.login(request);

    session.setAttribute("userId", loginResponse.getUserId()); // Key, Value
    session.setAttribute("name", loginResponse.getName());
    session.setAttribute("email", loginResponse.getEmail());

    log.info("session id : {}", session.getId());

    return ApiResponse.success();
  }

  @GetMapping("/status")
  public ApiResponse<LoginResponse> getStatus(HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");
    String name = (String) session.getAttribute("name");
    String email = (String) session.getAttribute("email");

    if (ObjectUtils.isEmpty(userId) && ObjectUtils.isEmpty(name) && ObjectUtils.isEmpty(email)) {
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA);
    }

    return ApiResponse.success(authService.getLoginResponse(userId, name, email));
  }

  @GetMapping("/logout")
  public ApiResponse<Void> logout(HttpSession httpSession) {
    httpSession.invalidate();
    return ApiResponse.success();
  }
}
