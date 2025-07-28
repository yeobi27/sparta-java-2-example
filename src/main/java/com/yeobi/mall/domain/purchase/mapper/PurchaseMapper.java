package com.yeobi.mall.domain.purchase.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

//  //source -> Purchase , target -> PurchaseResponse
//  @Mapping(source = "id", target = "productId")
//  @Mapping(source = "user.name", target = "username")
//  PurchaseProductResponse fromEntity(PurchaseProduct purchase);
}
