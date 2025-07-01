package com.sparta.java_02.domain.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
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
public class ProductRequest {

  @NotNull(message = "카테고리 ID는 필수입니다.")
  Long categoryId;
  @NotBlank(message = "상품 이름은 필수입니다.")
  String name;
  @NotNull(message = "가격은 필수입니다.")
  @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다.")
  BigDecimal price;

  @NotNull(message = "재고는 필수입니다.")
  @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
  Integer stock;

  String description;
}
