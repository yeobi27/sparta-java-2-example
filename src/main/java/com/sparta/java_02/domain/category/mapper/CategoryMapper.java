package com.sparta.java_02.domain.category.mapper;

import com.sparta.java_02.domain.category.dto.CategoryRequest;
import com.sparta.java_02.domain.category.dto.CategoryResponse;
import com.sparta.java_02.domain.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(source = "parent.id", target = "parentId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  CategoryResponse toResponse(Category category);

//  @Mapping(source = "parentId", target = "parent.id")
//  Category toEntity(CategoryRequest categoryRequest);
}
