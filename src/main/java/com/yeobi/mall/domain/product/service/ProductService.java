package com.yeobi.mall.domain.product.service;

import com.yeobi.mall.common.exception.CustomCheckedException;
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
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(
      ProductService.class);

  @Transactional
  public List<ProductResponse> searchByName(String keyword) {
    List<Product> products = productRepository.findByNameContaining(keyword);

    return products.stream()
        .map(productMapper::toResponse)
//        .toList();  // 리스트 수정을 안해도 될때
        .collect(Collectors.toList());  // 리스트 수정을 하려고 할때 .add(), .remove() 등등..
  }

  // 1. 상품 등록
  @Transactional
  public ProductResponse create(ProductRequest request) {
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
  @Transactional(readOnly = true)
  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream()
        .map(productMapper::toResponse)
        .collect(Collectors.toList());
  }

  // 2-1. 단일 항목 조회
  @Transactional
  public ProductResponse getById(Long id) {
    Product product = findProduct(id);
    return productMapper.toResponse(product);
  }

  // 3. 상품 수정
  @Transactional
  public ProductResponse update(Long id, ProductUpdateRequest request) {
    Product product = findProduct(id);
    productMapper.updateFromDto(request, product);
    //productRepository.save(product);
    return productMapper.toResponse(product); // save 생략 가능: JPA dirty checking
  }

  // 4. 제품 삭제
  @Transactional
  public void delete(Long id) {
    Product product = findProduct(id); // ← 이 시점에서 예외 처리를 포함한 검증 가능
    productRepository.delete(product); // ← 객체 삭제 방식
  }

  /**
   * 체크 예외(CustomCheckedException)가 발생하면, rollbackFor에 의해 트랜잭션이 롤백됩니다.
   */
  // Checked Exception 을 한 Transactional 에서 너무많이 사용할때는
  // 지양하는 방법이지만 rollbackFor = Exception.class 을 사용할때도 있다.
  // 아니면 try-catch 로 잡아서 런타임에러로 날려버리는 방법도 있다.
  @Transactional(rollbackFor = CustomCheckedException.class)
  public void updateProductStock(Long productId, Integer stock)
      throws CustomCheckedException {
    // 메서드 인자옆에 throws 는 에러처리를 위임한다 -> 에러를 바깥으로 빼준다.
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    log.info("상품 재고를 {}에서 {}로 변경 시도.", product.getStock(), stock);
    product.setStock(stock);
    productRepository.save(product); // 변경 사항을 우선 DB에 반영

    // 예외 발생 조건: 음수 가격은 허용하지 않음 (체크 예외)
    if (stock < 0) {
      // 에러를 발생시키기위한 throw
      try {
        throw new CustomCheckedException(ServiceExceptionCode.INSUFFICIENT_STOCK.getMessage());
      } catch (CustomCheckedException e) {
        throw new ServiceException(ServiceExceptionCode.INSUFFICIENT_STOCK);
      }

    }
  }

  /**
   * 언체크 예외(IllegalArgumentException)가 발생해도 noRollbackFor 설정 때문에 트랜잭션이 롤백되지 않습니다.
   */
  @Transactional(noRollbackFor = IllegalArgumentException.class)
  public void reduceProductStockNoRollback(Long productId, int quantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

    // 이 예제에서는 예외 발생 전 다른 DB 작업을 수행했다고 가정합니다.
    // ex) logRepository.save(new Log("재고 차감 시도..."));

    // 재고 부족 시 IllegalArgumentException 발생 (언체크 예외)
    if (product.getStock() < quantity) {
      throw new IllegalArgumentException(ServiceExceptionCode.INSUFFICIENT_STOCK.getMessage());
    }

    product.reduceStock(quantity);
    productRepository.save(product);
  }

  @Transactional
  private Category findCategory(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_EXIST_CATEGORY));
  }

  @Transactional
  private Product findProduct(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
  }
}
