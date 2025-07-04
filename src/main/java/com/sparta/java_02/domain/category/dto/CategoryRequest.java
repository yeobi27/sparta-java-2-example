package com.sparta.java_02.domain.category.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter             // Getter
@NoArgsConstructor  // 생성자
@AllArgsConstructor // 파라메터있는 생성자
@Builder            // Builder 패턴
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
  Long id;
  String name;
  Long parentId;
}
