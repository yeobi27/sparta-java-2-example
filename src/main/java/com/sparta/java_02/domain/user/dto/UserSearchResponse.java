package com.sparta.java_02.domain.user.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

// 다건으로사용되야함.
@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSearchResponse {

  private Long id;
  private String email;
  private String name;
  LocalDateTime createdAt;

}
