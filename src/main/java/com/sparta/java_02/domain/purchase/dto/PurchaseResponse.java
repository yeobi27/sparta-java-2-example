package com.sparta.java_02.domain.purchase.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/* 주문 완료 후, 주문 번호/주문 정보 등을 응답으로 내려줄 때 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseResponse {

  Long userId;
  Long productId;
  Integer quantity;
  String shippingAddress;

}
