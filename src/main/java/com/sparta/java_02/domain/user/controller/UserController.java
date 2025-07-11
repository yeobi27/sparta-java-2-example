package com.sparta.java_02.domain.user.controller;

import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.user.dto.UserCreateRequest;
import com.sparta.java_02.domain.user.dto.UserResponse;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.dto.UserUpdateRequest;
import com.sparta.java_02.domain.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 스프링컨테이너는 @Bean 에 등록된 것을 인스턴스화 해준다.
// 주입을 해야하는(Autowired) UserController 조차도 @Bean 으로 등록해줘야
// 스프링컨테이너가 아하! Bean 등록이 되어있구나! 한것들을 실행시켜준다.
// Bean 으로 등록해주는 동작은 @Component 하나다. 그래서 @Controller, @Repository, @Service 를 타고 들어가면 @Component 가 존재한다.
@RestController // @Controller, @ResponseBody 포함하고있다.
@RequiredArgsConstructor  // final 이 있는 변수를 찾아서 생성자로 만들어준다.
//  @Autowired
//  public UserController(UserService userService) {
//    this.userService = userService;
//  }
// 스프링컨테이너도 생성자 주입으로만 사용하다보니 @Autowired 도 생략해준다.
// 그래서 @RequiredArgsConstructor 를 사용하면 @Bean 에 자동으로 등록되는 형태이다.
@RequestMapping("/api/users") // 이를 붙이면 밑에 함수를 호출할때 붙는 경로가 공통적으로 붙는다.
public class UserController {

  private final UserService userService;

  @GetMapping
  public ApiResponse<List<UserSearchResponse>> findAll(
      @PathVariable Long userId,
      @RequestParam(name = "email", required = false) String email
  ) {
    return ApiResponse.success(userService.searchUser(userId));
    // error 동작은 Service 측에서 만들어두고, GlobalExceptionHandler 에 모두 모아두자!
  }

  // 2. 회원 단건 조회 (Read)
  @GetMapping("/{userId}")
  public ApiResponse<UserResponse> findById(@PathVariable Long userId) {
    UserResponse user = userService.getUserById(userId);
    return ApiResponse.success(user);
  }

  // RequestBody 는 Json 형태로 받기위해 사용
  // 1. 회원가입 (Create)
  @PostMapping
  public ApiResponse<Void> create(@Valid @RequestBody UserCreateRequest request) {
    userService.create(request);
    return ApiResponse.success();
  }

//  // 3. 회원 전체 조회 (조건: email 등 추가 가능)
//  @GetMapping
//  public ApiResponse<List<UserResponse>> getAllUsers(@RequestParam(required = false) String email) {
//    List<UserResponse> users = userService.getAllUsers(email);
//    return ApiResponse.success(users);
//  }

  // 4. 회원정보 수정 (Update)
  @PutMapping("/{userId}")
  public ApiResponse<UserResponse> update(
      @PathVariable Long userId,
      @Valid @RequestBody UserUpdateRequest request
  ) {
    userService.update(userId, request);
    return ApiResponse.success();
  }

  // 5. (선택) 회원 삭제(Soft Delete)
  @DeleteMapping("/hard/{userId}")
  public ApiResponse<String> delete(@PathVariable Long userId) {
    userService.delete(userId);
    return ApiResponse.success("사용자가 정상적으로 삭제되었습니다.");
  }
  @DeleteMapping("/{userId}")
  public ApiResponse<String> softDeleteUser(@PathVariable Long userId){
    userService.softDelete(userId);
    return ApiResponse.success("사용자가 정상적으로 비활성화되었습니다.");
  }
}