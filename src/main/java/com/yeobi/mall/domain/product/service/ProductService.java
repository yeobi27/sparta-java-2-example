package com.yeobi.mall.domain.product.service;

import com.yeobi.mall.common.exception.ServiceException;
import com.yeobi.mall.common.exception.ServiceExceptionCode;
import com.yeobi.mall.domain.category.entity.Category;
import com.yeobi.mall.domain.category.repository.CategoryRepository;
import com.yeobi.mall.domain.product.dto.ProductRequest;
import com.yeobi.mall.domain.product.dto.ProductResponse;
import com.yeobi.mall.domain.product.dto.ProductUpdateRequest;
import com.yeobi.mall.domain.product.entity.Product;
import com.yeobi.mall.domain.product.mapper.ProductMapper;
import com.yeobi.mall.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  public List<ProductResponse> searchByName(String keyword){
    List<Product> products = productRepository.findByNameContaining(keyword);

    return products.stream()
        .map(productMapper::toResponse)
//        .toList();  // 리스트 수정을 안해도 될때
        .collect(Collectors.toList());  // 리스트 수정을 하려고 할때 .add(), .remove() 등등..
  }

  // 1. 상품 등록
  @Transactional
  public ProductResponse create(ProductRequest request){
    Category category = findCategory(request.getCategoryId());
    Product product = productMapper.toEntity(request, category);
    Product savedProduct = productRepository.save(product);

    return productMapper.toResponse(savedProduct);
  }

  // 2. 전제 목록 조회
  //map(x -> something(x))	람다 표현식
  //map(product -> productMapper.toResponse(product))
  //map(ClassName::methodName)	메서드 참조 (= 축약 버전)
  // 즉, Product 리스트들 중 객체 하나씩 ProductResponse 로 
  // 바꾸는 작업을 하나씩 반복 처리한다는 뜻
  public List<ProductResponse> getAll(){
    return productRepository.findAll().stream()
        .map(productMapper::toResponse)
        .collect(Collectors.toList());
  }

  // 2-1. 단일 항목 조회
  public ProductResponse getById(Long id){
    Product product = findProduct(id);
    return productMapper.toResponse(product);
  }

  // 3. 상품 수정
  @Transactional
  public ProductResponse update(Long id, ProductUpdateRequest request){
    Product product = findProduct(id);
    productMapper.updateFromDto(request, product);
    //productRepository.save(product);
    return productMapper.toResponse(product); // save 생략 가능: JPA dirty checking
  }

  // 4. 제품 삭제
  @Transactional
  public void delete(Long id){
    Product product = findProduct(id); // ← 이 시점에서 예외 처리를 포함한 검증 가능
    productRepository.delete(product); // ← 객체 삭제 방식
  }

  private Category findCategory(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXIST_CATEGORY));
  }

  private Product findProduct(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
  }
}
