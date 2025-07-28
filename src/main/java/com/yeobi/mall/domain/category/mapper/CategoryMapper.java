package com.yeobi.mall.domain.category.mapper;

import com.yeobi.mall.domain.category.dto.CategoryRequest;
import com.yeobi.mall.domain.category.dto.CategoryResponse;
import com.yeobi.mall.domain.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(source = "parent.id", target = "parentId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "categories", ignore = true) // <- 경고 해결
  CategoryResponse toResponse(Category category);

  @Mapping(target = "parent", ignore = true) // <- 경고 해결
  Category toEntity(CategoryRequest categoryRequest);
}
