package com.sparta.java_02.domain.product.mapper;

import com.sparta.java_02.domain.category.entity.Category;
import com.sparta.java_02.domain.product.dto.ProductRequest;
import com.sparta.java_02.domain.product.dto.ProductResponse;
import com.sparta.java_02.domain.product.dto.ProductUpdateRequest;
import com.sparta.java_02.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

// DB에서 가져온 Product 엔티티(source)를 →
// ProductResponse(target)로 변환해서 →
// 클라이언트에게 응답
@Mapper(componentModel = "spring")
public interface  ProductMapper {
  // ProductRequest → Product 변환 (Category 별도로 주입)
  @Mapping(source = "request.name", target = "name")
  @Mapping(source = "request.description", target = "description")
  @Mapping(source = "request.price", target = "price")
  @Mapping(source = "request.stock", target = "stock")
  @Mapping(target = "category", source = "category") // Category 객체 자체 주입
  Product toEntity(ProductRequest request, Category category);
  // Product → ProductResponse
  @Mapping(source = "category.id", target = "categoryId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ProductResponse toResponse(Product product);
  // ProductUpdateRequest → 기존 Product 에 반영
  void updateFromDto(ProductUpdateRequest request, @MappingTarget Product product);
}
