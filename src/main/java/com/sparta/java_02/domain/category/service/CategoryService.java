package com.sparta.java_02.domain.category.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.common.utils.JedisUtils;
import com.sparta.java_02.domain.category.dto.CategoryRequest;
import com.sparta.java_02.domain.category.dto.CategoryResponse;
import com.sparta.java_02.domain.category.entity.Category;
import com.sparta.java_02.domain.category.mapper.CategoryMapper;
import com.sparta.java_02.domain.category.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.Jedis;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final Jedis jedis;
  private final ObjectMapper objectMapper;  // Config 에 추가해서 Bean 주입
  // 카테고리 생성,삭제,수정,조회
  // ex) 원하는 카테고리에 속한 하위 카테고리 수

  private final JedisUtils jedisUtils;
  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;
  private static final String CACHE_KEY_CATEGORY_STRUCT = "categoryStruct";
  private static final int CACHE_EXPIRE_SECONDS = 3600; // 1시간
  private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

  private List<CategoryResponse> findCategoryStruct() {

    List<Category> categories = categoryRepository.findAll();

    Map<Long, CategoryResponse> categoryResponseMap = new HashMap<>();

    for (Category category : categories) {
      CategoryResponse response = CategoryResponse.builder()
          .id(category.getId())
          .name(category.getName())
          .categories(new ArrayList<>())
          .build();

      categoryResponseMap.put(category.getId(), response);
    }

    List<CategoryResponse> rootCategories = new ArrayList<>();

    for (Category category : categories) {
      CategoryResponse categoryResponse = categoryResponseMap.get(category.getId());

      if (ObjectUtils.isEmpty(category.getParent())) {
        // 카테고리중에 최상위 객체니?
        rootCategories.add(categoryResponse);
      } else {
        CategoryResponse parentCategoryResponse = categoryResponseMap.get(
            category.getParent().getId());
        if (ObjectUtils.isEmpty(parentCategoryResponse)) {
          parentCategoryResponse.getCategories().add(categoryResponse);
        }
      }
    }
    return rootCategories;
  }

  @Transactional(readOnly = true)
  public List<CategoryResponse> findCategoryStructCacheAside() throws JsonProcessingException {
    // 1. 캐시에서 카테고리 구조 데이터 조회 시도
    String cachedCategories = jedis.get(CACHE_KEY_CATEGORY_STRUCT);

    try {
      // 2. 캐시 히트
      if (!ObjectUtils.isEmpty(cachedCategories)) {
        System.out.println("Cache Hit: categoryStruct for key " + CACHE_KEY_CATEGORY_STRUCT);
        // TypeReference 라는건 return 해주려는 타입으로 변경해서 돌려준다. 편하네?
        return objectMapper.readValue(cachedCategories,
            new TypeReference<List<CategoryResponse>>() {
            });
      }

      // 3. 캐시 미스, 데이터베이스에서 조회 (findCategoryStruct() 호출)
      System.out.println("Cache Miss: categoryStruct for key " + CACHE_KEY_CATEGORY_STRUCT);
      List<CategoryResponse> rootCategories = findCategoryStruct();

      // 4. 데이터베이스에서 조회한 데이터를 캐시에 저장
      if (!ObjectUtils.isEmpty(rootCategories)) {
        // 유틸로 변경!
        jedisUtils.saveObject(CACHE_KEY_CATEGORY_STRUCT, rootCategories, CACHE_EXPIRE_SECONDS);
//        String jsonString = objectMapper.writeValueAsString(rootCategories);
//        jedis.setex(CACHE_KEY_CATEGORY_STRUCT, CACHE_EXPIRE_SECONDS, jsonString);
      }

      return rootCategories; // 데이터베이스에서 조회한 데이터 반환
    } catch (Exception e) {
      throw new RuntimeException("JSON 파싱 버그");
    }
  }

  @Transactional
  public Boolean saveWriteThrough(CategoryRequest request) {
    Category parentCategory = ObjectUtils.isEmpty(request.getParentId()) ? null :
        categoryRepository.findById(request.getParentId())
            .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

    try {

      Category newCategory = Category.builder()
          .name(request.getName())
          .parent(parentCategory)
          .build();

      // 먼저 저장!
      categoryRepository.save(newCategory);

      updateCategoryStructCache(); // 캐시 업데이트 메서드 호출

      return true;
    } catch (Exception e) {
      log.error("Failed to save category with Write-through: {}", e.getMessage(), e);
      return false;
    }
  }

  private void updateCategoryStructCache() {
    try {
      // 전체조회해서 가져오고
      List<CategoryResponse> rootCategories = findCategoryStruct();

      if (!ObjectUtils.isEmpty(rootCategories)) {
        jedisUtils.saveObject(CACHE_KEY_CATEGORY_STRUCT, rootCategories, CACHE_EXPIRE_SECONDS);
//        // 값이 있다면 덮어쓰기
//        String jsonString = objectMapper.writeValueAsString(rootCategories);
//        // set , setex 차이점은 TTL 을 넣을수있냐 없냐 차이
//        jedis.setex(CACHE_KEY_CATEGORY_STRUCT, CACHE_EXPIRE_SECONDS, jsonString);
      }
    } catch (Exception e) {
      log.error("Error updating cache key {}: {}", CACHE_KEY_CATEGORY_STRUCT, e.getMessage());
    }
  }

  @Transactional
  public Boolean saveWriteBack(CategoryRequest request) {
    try {
      // 1. 캐시에서 현재 카테고리 구조를 조회(캐시에서)
      String cachedData = jedis.get(CACHE_KEY_CATEGORY_STRUCT);
      List<CategoryResponse> categories;  // 담을그릇

      if (cachedData != null && !cachedData.isEmpty()) {
        categories = objectMapper.readValue(cachedData,
            new TypeReference<List<CategoryResponse>>() {
            });
      } else {
        categories = new ArrayList<>();
      }

      // 2. 캐시에 새로운 카테고리 데이터 추가
      CategoryResponse newCategory = CategoryResponse.builder()
          .name(request.getName())
          .categories(new ArrayList<>())
          .build();

      // TODO: 신규 카테고리는 부모 클래스 하위로 들어가도록 수정되어야함.
      categories.add(newCategory);

      jedisUtils.saveObject(CACHE_KEY_CATEGORY_STRUCT, categories, CACHE_EXPIRE_SECONDS);

//      String jsonString = objectMapper.writeValueAsString(categories);
//      jedis.setex(CACHE_KEY_CATEGORY_STRUCT, CACHE_EXPIRE_SECONDS, jsonString);

      // 3. 데이터베이스 저장 작업은 비동기로 처리
      saveToDatabaseAsync(request);

      return true;
    } catch (Exception e) {
      log.error("Write-back 패턴 저장 실패: {}", e.getMessage(), e);
      return false;
    }
  }

  @Async
  public void saveToDatabaseAsync(CategoryRequest request) {
    try {
      Thread.sleep(2000);

      Category newCategory = Category.builder()
          .name(request.getName())
          .build();

      categoryRepository.save(newCategory);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("비동기 DB 저장 중 스레드 인터럽트: {}", e.getMessage(), e);
    } catch (Exception e) {
      log.error("비동기 DB 저장 실패: {}", e.getMessage(), e);
    }
  }

  @Transactional
  public CategoryResponse createCategory(CategoryRequest request) {
    Category parent = null;

    if (categoryRepository.existsByNameAndParentId(request.getName(), request.getParentId())) {
      throw new ServiceException(ServiceExceptionCode.DUPLICATED_CATEGORY);
    }

    // 부모 카테고리ID 가 있을 경우 조회하기
    if (request.getParentId() != null) {
      parent = categoryRepository.findById(request.getParentId())
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXIST_CATEGORY));
    }

    // Category 객체 생성( Builder )
    Category category = Category.builder()
        .name(request.getName())
        .parent(parent)
        .build();
    Category saved = categoryRepository.save(category);

//    CategoryRequest categoryRequest = CategoryRequest.builder()
//          .name(request.getName())
//          .parentId(parent != null ? parent.getId() : null)
//          .build();
    
//    Category category = categoryMapper.toEntity(categoryRequest);
//    Category saved = categoryRepository.save(category);

    return categoryMapper.toResponse(saved);
//    return null;
  }
}
