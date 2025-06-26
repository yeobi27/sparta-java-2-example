package com.sparta.java_02.domain.purchase.mapper;

import com.sparta.java_02.domain.purchase.dto.PurchaseResponse;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

  // source -> Purchase , target -> PurchaseResponse
  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "purchaseItems[0].product.id", target = "productId")
  @Mapping(source = "purchaseItems[0].quantity", target = "quantity")
  @Mapping(source = "shippingAddress", target = "shippingAddress")
  PurchaseResponse fromEntity(Purchase purchase);
}
