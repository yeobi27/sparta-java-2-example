package com.yeobi.mall.domain.purchase.service;

import com.yeobi.mall.common.enums.TaskType;
import com.yeobi.mall.common.exception.ServiceException;
import com.yeobi.mall.common.exception.ServiceExceptionCode;
import com.yeobi.mall.domain.product.repository.ProductRepository;
import com.yeobi.mall.domain.purchase.dto.PurchaseCancelRequest;
import com.yeobi.mall.domain.purchase.dto.PurchaseCancelResponse;
import com.yeobi.mall.domain.purchase.dto.PurchaseRequest;
import com.yeobi.mall.domain.purchase.entity.Purchase;
import com.yeobi.mall.domain.purchase.entity.PurchaseProduct;
import com.yeobi.mall.domain.purchase.mapper.PurchaseMapper;
import com.yeobi.mall.domain.purchase.repository.PurchaseRepository;
import com.yeobi.mall.domain.task.entity.TaskQueue;
import com.yeobi.mall.domain.task.repository.TaskQueueRepository;
import com.yeobi.mall.domain.task.service.TaskQueueService;
import com.yeobi.mall.domain.user.entity.User;
import com.yeobi.mall.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

  // 서비스들(도메인들)끼리는 순환참조가 일어나므로 직접의존성주입을 받아 사용한다거나
  // 되도록 Repository 를 받아서 사용하자.
  private final UserRepository userRepository;
  private final PurchaseRepository purchaseRepository;
  private final PurchaseProcessService purchaseProcessService;
  private final PurchaseCancelService purchaseCancelService;
  private final ProductRepository productRepository;
  private final PurchaseMapper purchaseMapper;

  private final TaskQueueService taskQueueService;
  private final TaskQueueRepository taskQueueRepository;

//  // 구매 로직 전체를 purchaseProcessService 로 따로 넘겨줌.
//  @Transactional
//  public PurchaseResponse placePurchase(PurchaseRequest request) {
//
//    // 1. 사용자 조회
//    User user = userRepository.findById(request.getUserId())
//        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
//    // 2. 상품 조회
//    Product product = productRepository.findById(request.getProductId())
//        .orElseThrow(
//            () -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));  // 상품 못찾음.
//    // 3. 재고 확인 및 감소 (핵심 비즈니스 로직)
//    if (product.getStock() < request.getQuantity()) {
//      throw new ServiceException(ServiceExceptionCode.INSUFFICIENT_STOCK);
//    }
//    product.reduceStock(request.getQuantity());
//
//    // 4. 구매(Purchase) 및 구매 항목(PurchaseItem) 생성 및 저장
//    // 여기서는 간단하게 단일 상품 주문만 처리하는 것으로 가정
//    Purchase purchase = Purchase.builder()
//        .user(user)
//        .totalPrice(product.getPrice().multiply(new BigDecimal(request.getQuantity())))
//        .status(PurchaseStatus.COMPLETED)
//        .shippingAddress(request.getShippingAddress())
//        .build();
//
//    PurchaseProduct item = PurchaseProduct.builder()
//        .purchase(purchase)
//        .product(product)
//        .quantity(request.getQuantity())
//        .price(product.getPrice()) // 주문 시점의 가격 기록
//        .build();
//
//    purchase.getPurchaseItems().add(item); // 연관관계 편의 메서드 활용
//
//    Purchase savedPurchase = purchaseRepository.save(purchase);
//
//    // 5. 응답 DTO로 변환하여 반환
////        return PurchaseResponse.fromEntity(savedPurchase);
//    return purchaseMapper.fromEntity(savedPurchase);
//  }

  @Transactional
  public void purchaseRequest(PurchaseRequest request) {
    TaskQueue taskQueue = taskQueueService.requestQueue(TaskType.PURCHASE);
    purchaseProcess(taskQueue.getId(), request);
  }

  @Async
  @Transactional
  public void purchaseProcess(Long taskQueueId, PurchaseRequest request) {
    taskQueueService.processQueueById(taskQueueId, (taskQueue) -> {
      User user = userRepository.findById(request.getUserId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

      Purchase purchase = purchaseProcessService.createAndSavePurchase(user);

      taskQueue.setEventId(purchase.getId());

      List<PurchaseProduct> purchaseProducts = purchaseProcessService.createAndProcessPurchaseProducts(
          request.getPurchaseProducts(),
          purchase);

      BigDecimal totalPrice = purchaseProcessService.calculateTotalPrice(purchaseProducts);
      purchase.setTotalPrice(totalPrice);
    });
  }

  @Transactional
  public Purchase purchase(PurchaseRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));

    return purchaseProcessService.process(user, request.getPurchaseProducts());
  }

  @Transactional
  public PurchaseCancelResponse cancel(PurchaseCancelRequest request) {
    User user = getUser(request.getUserId(), ServiceExceptionCode.NOT_FOUND_USER);
    // user 검증은 Auth 에서 수행 했다고 가정
    return purchaseCancelService.cancelPurchase(request.getPurchaseId(), request.getUserId());
  }

  public User getUser(Long userId, ServiceExceptionCode code) {
    return userRepository.findById(userId) // <- 기능
        .orElseThrow(() -> new ServiceException(code)); // 결과
  }

//  @Transactional
//  public void bulkUpdateStatus() {
//    purchaseRepository.bulkUpdateStatus(LocalDateTime.now());
//  }
}

