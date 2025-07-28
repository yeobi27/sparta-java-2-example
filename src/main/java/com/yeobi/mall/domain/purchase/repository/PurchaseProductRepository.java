package com.yeobi.mall.domain.purchase.repository;

import com.yeobi.mall.domain.purchase.entity.PurchaseProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {

  List<PurchaseProduct> findByPurchase_Id(Long purchaseId);
}
