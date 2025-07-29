package com.yeobi.mall.domain.product.service;

import com.yeobi.mall.common.exception.CustomCheckedException;
import com.yeobi.mall.domain.product.entity.Product;
import com.yeobi.mall.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

  private static final Logger log = LoggerFactory.getLogger(ProductServiceTest.class);

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  // rollbackFor = CustomCheckedException.class 설정이면 롤백이 된다.
  // 개발자의 의도대로 만들면된다.
  @Test
  @DisplayName("체크 예외 발생 시 rollbackFor 설정으로 트랜잭션이 롤백된다")
  void testRollbackForCheckedException() {
    // given: 초기 데이터 준비
    Long productId = 1L;
    Product originalProduct = productRepository.findById(productId).orElseThrow();
    Integer originalStock = originalProduct.getStock();

    // when & then: 음수 가격 업데이트 시도 -> CustomCheckedException 발생을 기대
    Assertions.assertThrows(CustomCheckedException.class, () -> {
      productService.updateProductStock(productId, -1000);
    });

    // then: 롤백이 정상적으로 수행되어 가격이 원래대로 복구되었는지 확인
    Product productAfterRollback = productRepository.findById(productId).orElseThrow();
    Assertions.assertEquals(originalStock, productAfterRollback.getStock());
    log.info("롤백 후 가격: {}, 정상 복구됨.", productAfterRollback.getStock());
  }
}