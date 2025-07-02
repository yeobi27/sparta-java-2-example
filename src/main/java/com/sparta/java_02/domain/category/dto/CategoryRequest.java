package com.sparta.java_02.domain.category.dto;

import com.sparta.java_02.domain.category.entity.Category;
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
//  Category parent;  // 가장 상위 카테고리면 null 타입이어야할텐데..?
  Long parentId;
}
