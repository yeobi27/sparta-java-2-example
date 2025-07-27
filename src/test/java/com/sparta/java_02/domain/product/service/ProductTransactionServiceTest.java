package com.sparta.java_02.domain.product.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductTransactionServiceTest {

  @Autowired
  private ProductTransactionService productTransactionService;

  @Autowired
  private ProductRepository productRepository;

  @Test
  void testUpdateProductStockSuccess() {
    // given
    Long productId = 1L;
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    // when
    productTransactionService.updateProductStock(product.getId(), 3);

    // then
    Product resultProduct = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    Assertions.assertEquals(product.getStock(), resultProduct.getStock() + 3);
  }

  @Test
  void testUpdateProductStockSuccessTransactional() {
    // given
    Long productId = 1L;
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    // when
    productTransactionService.updateProductStockTransactional(product.getId(), 3);

    // then
    Product resultProduct = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    Assertions.assertEquals(product.getStock(), resultProduct.getStock() + 3);
  }

}