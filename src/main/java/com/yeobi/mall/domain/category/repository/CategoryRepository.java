package com.yeobi.mall.domain.category.repository;

import com.yeobi.mall.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  boolean existsByNameAndParentId(String name, Long parentId);
}
