package com.sparta.java_02.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ServiceExceptionCode {
  NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다."),
  INSUFFICIENT_STOCK("상품의 재고가 부족합니다."),
  NOT_FOUND_USER("유저를 찾을 수 없습니다."),
  DUPLICATE_EMAIL("이메일이 중복되었습니다."),
  NOT_EXIST_CATEGORY("해당 카테고리가 존재하지 않습니다.");
  // ... 다른 예외 코드들

  final String message;
}