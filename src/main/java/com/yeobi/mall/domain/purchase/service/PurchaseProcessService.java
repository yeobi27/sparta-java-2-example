package com.yeobi.mall.domain.purchase.service;

import com.yeobi.mall.common.enums.PurchaseStatus;
import com.yeobi.mall.common.exception.ServiceException;
import com.yeobi.mall.common.exception.ServiceExceptionCode;
import com.yeobi.mall.domain.product.entity.Product;
import com.yeobi.mall.domain.product.repository.ProductRepository;
import com.yeobi.mall.domain.purchase.dto.PurchaseProductRequest;
import com.yeobi.mall.domain.purchase.entity.Purchase;
import com.yeobi.mall.domain.purchase.entity.PurchaseProduct;
import com.yeobi.mall.domain.purchase.repository.PurchaseProductRepository;
import com.yeobi.mall.domain.purchase.repository.PurchaseRepository;
import com.yeobi.mall.domain.user.entity.User;
import com.yeobi.mall.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseProcessService {

  private final PurchaseRepository purchaseRepository;
  private final ProductRepository productRepository;
  private final PurchaseProductRepository purchaseProductRepository;
  private final UserRepository userRepository;

  public Purchase process(User user, List<PurchaseProductRequest> requests) {
    // 이제 purchase 메서드는 "무엇을 하는지" 명확히 보여준다.
    Purchase purchase = createAndSavePurchase(user);
    List<PurchaseProduct> purchaseProducts = createAndProcessPurchaseProducts(requests,
        purchase);
    BigDecimal totalPrice = calculateTotalPrice(purchaseProducts);

    purchase.setTotalPrice(totalPrice);
    return purchase;
  }

  // 각 메서드는 "어떻게 하는지" 구체적인 책임을 가진다.
  public Purchase createAndSavePurchase(User user) {
    return purchaseRepository.save(Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.ZERO)
        .status(PurchaseStatus.COMPLETED)
        .build());
  }

  public List<PurchaseProduct> createAndProcessPurchaseProducts(
      List<PurchaseProductRequest> itemRequests, Purchase purchase) {
    List<PurchaseProduct> purchaseProducts = new ArrayList<>();

    for (PurchaseProductRequest itemRequest : itemRequests) {
      Product product = productRepository.findById(itemRequest.getProductId()).orElseThrow();

      validateStock(product, itemRequest.getQuantity());
      product.reduceStock(itemRequest.getQuantity());

      PurchaseProduct purchaseProduct = PurchaseProduct.builder()
          .product(product)
          .purchase(purchase)
          .quantity(itemRequest.getQuantity())
          .price(product.getPrice())
          .build();

      purchaseProducts.add(purchaseProduct);
    }

    purchaseProductRepository.saveAll(purchaseProducts);
    return purchaseProducts;
  }

  public void validateStock(Product product, int requestedQuantity) {
    if (requestedQuantity > product.getStock()) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }
  }

  public BigDecimal calculateTotalPrice(List<PurchaseProduct> purchaseProducts) {
    return purchaseProducts.stream()
        .map(purchaseProduct -> purchaseProduct.getPrice()
            .multiply(BigDecimal.valueOf(purchaseProduct.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
