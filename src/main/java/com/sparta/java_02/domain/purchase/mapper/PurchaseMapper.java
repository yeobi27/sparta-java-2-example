package com.sparta.java_02.domain.purchase.mapper;

import com.sparta.java_02.domain.purchase.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

  //  //source -> Purchase , target -> PurchaseResponse
  @Mapping(source = "id", target = "purchaseId")
  @Mapping(source = "user.name", target = "username")
  @Mapping(source = "shippingAddress", target = "shippingAddress")
  PurchaseResponse fromEntity(Purchase purchase);
}
