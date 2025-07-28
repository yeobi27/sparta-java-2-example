package com.yeobi.mall.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPurchaseResponse {

  Long id;
  String name;
  String email;
  Long purchaseName;
  BigDecimal purchaseTotalPrice;

  /*- 두개 이상 엔티티에서 필요한 필드를 뽑아서
  별도 DTO로 만들어 사용하기 위함*/
  // UserQueryRepository 에서 사용했음.
  @QueryProjection
  public UserPurchaseResponse(Long id, String name, String email, Long purchaseName,
      BigDecimal purchaseTotalPrice) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.purchaseName = purchaseName;
    this.purchaseTotalPrice = purchaseTotalPrice;
  }
}
