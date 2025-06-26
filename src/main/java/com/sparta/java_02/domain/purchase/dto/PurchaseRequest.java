package com.sparta.java_02.domain.purchase.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/* 사용자가 "구매하기" 버튼을 눌렀을 때 프론트에서 전달해주는 값 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseRequest {

  Long userId;
  Long productId;
  Integer quantity;
  String shippingAddress;
}
