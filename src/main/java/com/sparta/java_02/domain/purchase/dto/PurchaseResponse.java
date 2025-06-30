package com.sparta.java_02.domain.purchase.dto;

import java.math.BigDecimal;
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

  Long purchaseId; // Entity의 'id'와 이름이 다름
  String username;   // Entity의 'user.username'에서 가져와야 함
  BigDecimal totalPrice;
  String shippingAddress;

//  Long userId;
//  Long purchaseId;
//  Integer quantity;
//  String shippingAddress;

}
