package com.sparta.java_02.domain.purchase.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.entity.PurchaseProduct;
import com.sparta.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
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

  public Purchase process(User user, List<PurchaseProductRequest> purchaseItems) {
    // 이제 purchase 메서드는 "무엇을 하는지" 명확히 보여준다.
    Purchase purchase = createAndSavePurchase(user);
    List<PurchaseProduct> purchaseProducts = createAndProcessPurchaseProducts(purchaseItems,
        purchase);
    BigDecimal totalPrice = calculateTotalPrice(purchaseProducts);

    purchase.setTotalPrice(totalPrice);
    return purchase;
  }

  // 각 메서드는 "어떻게 하는지" 구체적인 책임을 가진다.
  private Purchase createAndSavePurchase(User user) {
    return purchaseRepository.save(Purchase.builder()
        .user(user)
        .build());
  }

  private List<PurchaseProduct> createAndProcessPurchaseProducts(
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

  private void validateStock(Product product, int requestedQuantity) {
    if (requestedQuantity > product.getStock()) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }
  }

  private BigDecimal calculateTotalPrice(List<PurchaseProduct> purchaseProducts) {
    return purchaseProducts.stream()
        .map(purchaseProduct -> purchaseProduct.getPrice()
            .multiply(BigDecimal.valueOf(purchaseProduct.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
