package com.sparta.java_02.domain.purchase.repository;

import com.sparta.java_02.domain.purchase.entity.PurchaseProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {

  List<PurchaseProduct> findByPurchase_Id(Long purchaseId);
}
