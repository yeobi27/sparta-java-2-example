package com.yeobi.mall.domain.product.entity;

import com.yeobi.mall.common.exception.ServiceException;
import com.yeobi.mall.common.exception.ServiceExceptionCode;
import com.yeobi.mall.domain.category.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {  // 상품

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private Integer stock;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column
  LocalDateTime updatedAt;

  @Version  // 낙관적 락을 위한 버전 관리
  private Integer version;

  @Builder
  public Product(String name, String description, BigDecimal price, Integer stock,
      Category category) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
    this.category = category;
  }

  public void reduceStock(int quantity) {
    if (this.stock < quantity) {
      throw new ServiceException(ServiceExceptionCode.INSUFFICIENT_STOCK);
    }
    this.stock -= quantity;
  }

  public void increaseStock(int quantity) {
    if (this.stock < quantity) {
      throw new ServiceException(ServiceExceptionCode.INSUFFICIENT_STOCK);
    }
    this.stock += quantity;
  }

  // TODO : 시험용 테스트 케이스 후에 지울것
  public void setStock(int newStock) {
    if (stock > 0) {
      this.stock = newStock;
    }
  }
}
