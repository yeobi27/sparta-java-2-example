package com.yeobi.mall.domain.purchase.dto;

import java.util.List;

public class PurchaseRequestTest {

  Long userId;
  List<PurchaseProductRequestTest> purchaseProducts;

  public PurchaseRequestTest(Long userId, List<PurchaseProductRequestTest> purchaseProducts) {
    this.userId = userId;
    this.purchaseProducts = purchaseProducts;
  }

  public Long getUserId() {
    return userId;
  }

  public List<PurchaseProductRequestTest> getPurchaseProducts() {
    return purchaseProducts;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setPurchaseProducts(
      List<PurchaseProductRequestTest> purchaseProducts) {
    this.purchaseProducts = purchaseProducts;
  }
}
