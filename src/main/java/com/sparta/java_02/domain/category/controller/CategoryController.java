package com.sparta.java_02.domain.category.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.category.dto.CategoryRequest;
import com.sparta.java_02.domain.category.dto.CategoryResponse;
import com.sparta.java_02.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")  // 관리자만 접근 가능
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
//  @PreAuthorize("hasRole('ADMIN')")
  public ApiResponse<CategoryResponse> create(@RequestBody @Valid CategoryRequest request) {
    return ApiResponse.success(categoryService.createCategory(request));
  }
  @GetMapping
  public ApiResponse<List<CategoryResponse>> getCategories() throws JsonProcessingException {
    return ApiResponse.success(categoryService.findCategoryStructCacheAside());
  }
}
