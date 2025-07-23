package com.sparta.java_02.domain.user.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchUserDto {

  Long id;
  String name;
  String email;
  String passwordHash;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}
