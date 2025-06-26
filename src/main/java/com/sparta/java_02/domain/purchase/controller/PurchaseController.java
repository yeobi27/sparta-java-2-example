package com.sparta.java_02.domain.purchase.controller;

import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequest;
import com.sparta.java_02.domain.purchase.dto.PurchaseResponse;
import com.sparta.java_02.domain.purchase.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchases")
public class PurchaseController {

  private final PurchaseService purchaseService;

  // 구매 로직 전체를 책임 "구매하기" 눌러서 요청
  @PostMapping
  public ApiResponse<PurchaseResponse> placePurchase(@Valid @RequestBody PurchaseRequest request) {
    purchaseService.placePurchase(request);
    return ApiResponse.success();
  }
}
