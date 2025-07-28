package com.yeobi.mall.domain.purchase.service;

import com.yeobi.mall.common.constants.Constants;
import com.yeobi.mall.common.enums.PurchaseStatus;
import com.yeobi.mall.common.exception.ServiceException;
import com.yeobi.mall.common.exception.ServiceExceptionCode;
import com.yeobi.mall.domain.product.entity.Product;
import com.yeobi.mall.domain.product.repository.ProductRepository;
import com.yeobi.mall.domain.purchase.dto.PurchaseCancelResponse;
import com.yeobi.mall.domain.purchase.dto.PurchaseProductResponse;
import com.yeobi.mall.domain.purchase.entity.Purchase;
import com.yeobi.mall.domain.purchase.entity.PurchaseProduct;
import com.yeobi.mall.domain.purchase.repository.PurchaseProductRepository;
import com.yeobi.mall.domain.purchase.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseCancelService {

  private final PurchaseRepository purchaseRepository;
  private final PurchaseProductRepository purchaseProductRepository;
  private final ProductRepository productRepository;

  public PurchaseCancelResponse cancelPurchase(Long purchaseId, Long userId) {
    // 1. 구매 정보 조회 및 권한 확인
    Purchase purchase = purchaseRepository.findByIdAndUser_Id(purchaseId, userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXIST_CATEGORY));

    // 2. 취소 가능 여부 확인 및 상태 변경
    if (purchase.getStatus() != PurchaseStatus.PENDING) {
      throw new ServiceException(ServiceExceptionCode.CANNOT_CANCEL);
    }
    purchase.setStatus(PurchaseStatus.CANCELED); // 취소 불가능한 상태인 경우 예외 재던지기

    // 3. 구매 상품 목록 조회
    List<PurchaseProduct> purchaseProducts = purchaseProductRepository.findByPurchase_Id(
        purchaseId);
    // 4. 재고 복원
    restoreProductStock(purchaseProducts);

    List<PurchaseProductResponse> PurchaseProductResponses = getPurchaseProductResponses(
        purchaseProducts);

    return PurchaseCancelResponse.builder()
        .purchaseId(purchase.getId())
        .status(purchase.getStatus())
        .cancelledAt(purchase.getUpdatedAt())
        .cancelledProducts(PurchaseProductResponses)
        .message(Constants.PURCHASE_CANCEL_MESSAGE)
        .build();
  }

  private void restoreProductStock(List<PurchaseProduct> purchaseProducts) {
    for (PurchaseProduct purchaseProduct : purchaseProducts) {
      Product product = purchaseProduct.getProduct();

      // 재고 복원 (현재 재고 + 취소된 수량)
      product.increaseStock(purchaseProduct.getQuantity());

      productRepository.save(product);
    }
  }

  private void validatePurchaseStatus(Purchase purchase) {
    if (purchase.getStatus() != PurchaseStatus.PENDING) {
      throw new ServiceException(ServiceExceptionCode.CANNOT_CANCEL);
    }
    purchase.setStatus(PurchaseStatus.CANCELED); // 취소 불가능한 상태인 경우 예외 재던지기
  }

  private List<PurchaseProductResponse> getPurchaseProductResponses(
      List<PurchaseProduct> purchaseProducts) {
    return purchaseProducts.stream()
        .map((purchaseProduct) -> {
          Product product = purchaseProduct.getProduct();
          BigDecimal totalPrice = purchaseProduct.getPrice()
              .multiply(BigDecimal.valueOf(purchaseProduct.getQuantity()));

          return PurchaseProductResponse.builder()
              .productId(product.getId())
              .productName(product.getName())
              .quantity(purchaseProduct.getQuantity())
              .price(purchaseProduct.getPrice())
              .totalPrice(totalPrice)
              .build();
        }).toList();
  }
}
