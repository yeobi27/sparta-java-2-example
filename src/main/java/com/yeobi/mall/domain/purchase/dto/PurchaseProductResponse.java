package com.yeobi.mall.domain.purchase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 JSON에서 제외
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseProductResponse {

  Long productId;

  String productName;

  Integer quantity;

  BigDecimal price;

  BigDecimal totalPrice;

}
