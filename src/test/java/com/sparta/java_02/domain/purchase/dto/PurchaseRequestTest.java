package com.sparta.java_02.domain.purchase.dto;

import java.util.List;

public class PurchaseRequestTest {

  Long userId;
  List<PurchaseProductRequestTest> purchaseItems;

  public PurchaseRequestTest(Long userId, List<PurchaseProductRequestTest> purchaseItems) {
    this.userId = userId;
    this.purchaseItems = purchaseItems;
  }

  public Long getUserId() {
    return userId;
  }

  public List<PurchaseProductRequestTest> getPurchaseItems() {
    return purchaseItems;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setPurchaseItems(
      List<PurchaseProductRequestTest> purchaseItems) {
    this.purchaseItems = purchaseItems;
  }
}
