package com.yeobi.mall.domain.product.service;

import com.yeobi.mall.common.exception.ServiceException;
import com.yeobi.mall.common.exception.ServiceExceptionCode;
import com.yeobi.mall.domain.product.entity.Product;
import com.yeobi.mall.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLockService {

  private final ProductRepository productRepository;

  // 비관적 락 시도
  @Transactional
  public void updateStockWithPessimisticLock(Long productId, Integer quantity) {
    Product product = productRepository.findByIdForUpdate(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    if (product.getStock() < quantity) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }

    product.setStock(product.getStock() - quantity);
    productRepository.save(product);
  }

  // 낙관적 락 시도
  @Transactional
  public void updateStockWithOptimisticLock(Long productId, int quantity) {
    Product product = productRepository.findByIdOrderById(productId)
        .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

    if (product.getStock() < quantity) {
      throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK_PRODUCT);
    }

    product.setStock(product.getStock() - quantity);
    productRepository.save(product);
  }

}