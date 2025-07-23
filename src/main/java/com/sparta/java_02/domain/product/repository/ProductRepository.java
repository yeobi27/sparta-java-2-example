package com.sparta.java_02.domain.product.repository;

import com.sparta.java_02.domain.product.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByName(String name);

  List<Product> findByPriceGreaterThan(BigDecimal price);

  List<Product> findByNameContaining(String keyword);

  List<Product> findByNameAndStock(String name, int stock);

  @Query("SELECT p FROM Product p WHERE p.price >= :minPrice AND p.name LIKE %:name%")
  List<Product> searchByNameAndMinPrice(@Param("name") String name,
      @Param("minPrice") BigDecimal minPrice);

  // 인자로 들어온 값보다 큰 경우
  List<Product> findAllByStockGreaterThan(Integer stock);
}
