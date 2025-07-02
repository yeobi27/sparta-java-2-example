package com.sparta.java_02.domain.category.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.category.dto.CategoryRequest;
import com.sparta.java_02.domain.category.dto.CategoryResponse;
import com.sparta.java_02.domain.category.entity.Category;
import com.sparta.java_02.domain.category.mapper.CategoryMapper;
import com.sparta.java_02.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  // 카테고리 생성,삭제,수정,조회
  // ex) 원하는 카테고리에 속한 하위 카테고리 수

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public CategoryResponse createCategory(CategoryRequest request) {
    Category parent = null;

    // 부모 카테고리ID 가 있을 경우 조회하기
    if (request.getParentId() != null) {
      parent = categoryRepository.findById(request.getParentId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXIST_CATEGORY));
    }

//    // Category 객체 생성( Builder )
//    Category category = Category.builder()
//        .name(request.getName())
//        .parent(parent)
//        .build();
    Category category = categoryMapper.toEntity(request);
    Category saved = categoryRepository.save(category);
    return categoryMapper.toResponse(saved);
  }
}
