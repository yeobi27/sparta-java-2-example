package com.sparta.java_02.domain.purchase.service;

// assert, mockito 주의하면서 받기

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.java_02.common.enums.PurchaseStatus;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.repository.ProductRepository;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductRequest;
import com.sparta.java_02.domain.purchase.entity.Purchase;
import com.sparta.java_02.domain.purchase.repository.PurchaseProductRepository;
import com.sparta.java_02.domain.purchase.repository.PurchaseRepository;
import com.sparta.java_02.domain.user.entity.User;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PurchaseProcessServiceTest {

  /* PurchaseProcessService 가 주입받고있는 필드를 모두 가져와야한다. */
  @InjectMocks  // <- Bean 이 아닌 mock Object 가 들어옴. (가짜)
  private PurchaseProcessService purchaseProcessService;
  @Mock
  private PurchaseRepository purchaseRepository;
  @Mock
  private ProductRepository productRepository;
  @Mock
  private PurchaseProductRepository purchaseProductRepository;

  private User testUser;
  private Purchase testPurchase;
  private Product testProduct;

  @BeforeEach
  void setUp() {
    testUser = User.builder()
        .name("테스트사용자")
        .email("test@example.com")
        .passwordHash("hashedPassword")
        .build();

    // 원래 로직 수정없이
    // ReflectionTestUtils.setField 하게되면 필드에 임의로 주입을 해준다. User 안에 id 라는곳은 실제로는 넣어지지 않는것인데, 넣게해준다. 대박적
    // 테스트케이스에서만 가능한 객체다.
    ReflectionTestUtils.setField(testUser, "id", 1L);

    testProduct = Product.builder()
        .name("노트북")
        .price(new BigDecimal("1000000")) // 100만원
        .stock(10)
        .build();

    ReflectionTestUtils.setField(testProduct, "id", 1L);

    testPurchase = Purchase.builder()
        .user(testUser)
        .totalPrice(BigDecimal.ZERO)
        .status(PurchaseStatus.PENDING)
        .build();

    ReflectionTestUtils.setField(testPurchase, "id", 1L);
  }

  @Test
  @DisplayName("재고가 충분한 상품을 구매하면 재고가 감소하고 구매가 성공한다")
  void process_should_decreaseStockAndSucceed_when_productInStock_gwt() {
    // given
    PurchaseProductRequest purchaseItem = new PurchaseProductRequest();
    ReflectionTestUtils.setField(purchaseItem, "productId", 1L);
    ReflectionTestUtils.setField(purchaseItem, "quantity", 2);

    List<PurchaseProductRequest> purchaseItems = List.of(purchaseItem);

    // when() 은 mockito 에서 시뮬하는것
    // findById, save, saveAll 메소드가 동작이 제대로 되는지 검증하는 것이다.
    // 설명 더 하자면 productRepository.findById(1L) 를 실행하면 Optional.of(testProduct) 타입을 반환하고 정상동작하는가?? 하는것임
    // 말 그대로 검증하는 동작임
    // id 로 1을 찾으면 testProduct 라는 객체를 가져오는가
    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    // 구매생성을하면 구매한건을 가지고오는가
    when(purchaseRepository.save(any(Purchase.class))).thenReturn(testPurchase);
    // anyList 값이 있는 임의의 리스트를 saveAll 하면 로직상 리턴값이 빈값이니까. 빈값으로 전달을 해주는가를 확인
    when(purchaseProductRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

    // when & then
    Purchase purchase = purchaseProcessService.process(testUser, purchaseItems);

    assertThat(purchase).isNotNull(); // null 이면 통과하지못함.
    assertThat(purchase.getTotalPrice()).isEqualTo(
        new BigDecimal("2000000")); // 1,000,000 * 2 <- 현재 100만원인데 200만원이라서 다르면 통과못함
    assertThat(testProduct.getStock()).isEqualTo(8); // 10 - 2 <- 10개중 1개를 구매를 했는데 9개가 아니면? 통과못함.

    // 특정(findById, save, saveAll ..)메소드가 실제로 실행이 됐는지 각각 체크함.
    // 위에 when() 이 감싸고 있는 메소드가 실행이 됐는지 체크
    verify(productRepository).findById(1L);
    verify(purchaseRepository).save(any(Purchase.class));
    verify(purchaseProductRepository).saveAll(anyList());
  }

  @Test
  @DisplayName("재고가 0인 상품을 구매하려고 하면 OUT_OF_STOCK_PRODUCT 예외가 발생한다")
  void process_should_throwsOutOfStockException_when_zeroStock() {
    // Given
    testProduct = Product.builder()
        .name("품절상품")
        .price(new BigDecimal("1000000"))
        .stock(0) // 재고 0
        .build();
    ReflectionTestUtils.setField(testProduct, "id", 1L);

    PurchaseProductRequest purchaseItem = new PurchaseProductRequest();
    ReflectionTestUtils.setField(purchaseItem, "productId", 1L);
    ReflectionTestUtils.setField(purchaseItem, "quantity", 10);

    List<PurchaseProductRequest> purchaseItems = List.of(purchaseItem);

    when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    when(purchaseRepository.save(any(Purchase.class))).thenReturn(testPurchase); // Mock 설정 추가

    // When & Then
    ServiceException exception = assertThrows(ServiceException.class, () -> {
      // 현재 재고가 0 인데, 10개를 주문하면 에러가 나는게 당연하다.
      // 그래서 에러가 나는지 if 문으로 체크하는 부분이다. 에러가 난다면 process 를 패스
      purchaseProcessService.process(testUser, purchaseItems);
    });

    assertThat(exception.getCode()).isEqualTo("OUT_OF_STOCK_PRODUCT");
    assertThat(testProduct.getStock()).isEqualTo(0); // 재고는 그대로 0

    verify(productRepository).findById(1L);
    verify(purchaseRepository, times(1)).save(any(Purchase.class)); // Purchase는 저장됨
    // never() 를 넣어서 saveAll 이 실행이 안됐는지를 체크
    // 실제로 안쓰고있음.
    verify(purchaseProductRepository, never()).saveAll(anyList()); // PurchaseProduct는 저장되지 않음
  }
}