package com.sparta.java_02.domain.purchase.repository;

import com.sparta.java_02.common.enums.PurchaseStatus;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
public class PurchaseRepositoryTest {

  @Autowired
  private PurchaseRepository purchaseRepository;
  @Autowired
  private UserRepository userRepository;

  @Test
  void save() {
    User user = User.builder()
        .name("d1")
        .email("d1")
        .passwordHash("d1")
        .build();

    userRepository.save(user);  // 먼저 저장해야 영속 상태로 변경됨

    Purchase purchase = Purchase.builder()
        .user(user)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase = purchaseRepository.save(purchase);

  }

  @Test
  void 수정() {

  }

  @Test
  void 삭제() {

  }

  @Test
  void 조회() {
    // 세이브부터
    User user1 = User.builder()
        .name("d1")
        .email("d1")
        .passwordHash("d1")
        .build();

    userRepository.save(user1);  // 먼저 저장해야 영속 상태로 변경됨

    Purchase purchase1 = Purchase.builder()
        .user(user1)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase1 = purchaseRepository.save(purchase1);

    User user2 = User.builder()
        .name("d2")
        .email("d2")
        .passwordHash("d2")
        .build();

    userRepository.save(user2);  // 먼저 저장해야 영속 상태로 변경됨

    Purchase purchase2 = Purchase.builder()
        .user(user2)
        .totalPrice(BigDecimal.valueOf(1000))
        .status(PurchaseStatus.PENDING)
        .build();

    Purchase savePurchase2 = purchaseRepository.save(purchase2);

    List<Purchase> purchases = purchaseRepository.findAll();
//    Purchase purchase = purchaseRepository.findById(7L)
//        .orElseThrow(() -> new RuntimeException("주문내역이 없음"));

//    System.out.println("결과 + " + purchases.get(0).getId());
    System.out.println("결과 + " + purchases.get(0).getId());
  }
}
