package com.sparta.java_02.domain.purchase.repository;

import com.sparta.java_02.domain.purchase.entity.Purchase;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

  Optional<Purchase> findByIdAndUser_Id(Long purchaseId, Long userId);

//   모든 'PENDING' 상태의 주문을 'COMPLETED'로 한번에 변경
//  @Modifying(clearAutomatically = true) // 쿼리 실행 후 영속성 컨텍스트를 자동으로 clear
//  @Query("UPDATE Purchase p SET p.status = 'COMPLETED' WHERE p.createdAt < :date AND p.status = 'PENDING'")
//  int bulkUpdateStatus(@Param("date") LocalDateTime date);
}
