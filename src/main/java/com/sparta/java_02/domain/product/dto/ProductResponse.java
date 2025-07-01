package com.sparta.java_02.domain.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

  private Long id;
  private Long categoryId;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer stock;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
