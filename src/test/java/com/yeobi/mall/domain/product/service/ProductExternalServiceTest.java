package com.yeobi.mall.domain.product.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@Transactional
@Rollback(false) // <-- rollback을 막아서 실제 저장
@SpringBootTest
class ProductExternalServiceTest {

  @Autowired
  private ProductExternalService productExternalService;

  @Test
  void save() {
    productExternalService.save();
  }

  @Test
  void saveAll() {
    productExternalService.saveAllExternalProducts();
  }
}