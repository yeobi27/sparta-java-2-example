package com.yeobi.mall.domain.product.service;

import com.yeobi.mall.common.exception.ServiceException;
import com.yeobi.mall.common.exception.ServiceExceptionCode;
import com.yeobi.mall.domain.category.entity.Category;
import com.yeobi.mall.domain.category.repository.CategoryRepository;
import com.yeobi.mall.domain.product.entity.Product;
import com.yeobi.mall.domain.product.repository.ProductRepository;
import com.yeobi.mall.global.external.client.ExternalShopClient;
import com.yeobi.mall.global.external.dto.ExternalProductResponse;
import com.yeobi.mall.global.external.dto.ExternalProductResponse.ExternalResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductExternalService {

  private final ExternalShopClient externalShopClient;
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Transactional
  @Retryable(value = {
      ServiceException.class,
      IOException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
  public void save() {
    try {
      // 조회는 제일 처음 호출이 좋음
      // POST , DELETE, PUT 은 맨 마지막이 좋음
      ExternalProductResponse responses = externalShopClient.getProducts(1, 10);
      log.info("response : {} ", responses.toString());

      List<ExternalResponse> contents = responses.getMessage().getContents();

      if (contents.isEmpty()) {
        throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
      }

      Category category = categoryRepository.findById(1L)
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

      List<Product> products = new ArrayList<>();
      for (ExternalResponse externalProduct : contents) {
        products.add(Product.builder()
            .name(externalProduct.getName())
            .description(externalProduct.getDescription())
            .stock(externalProduct.getStock())
            .price(externalProduct.getPrice())
            .category(category)
            .build());
      }
      productRepository.saveAll(products);

    } catch (Exception error) {
      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
    }
  }

  @Transactional
  @Retryable(value = ServiceException.class, maxAttempts = 10, backoff = @Backoff(delay = 1000))
  public void saveAllExternalProducts() {
    int page = 0;
    int pageSize = 10;
    boolean lastPage = false;

    while (!lastPage) {
      ExternalProductResponse responses = externalShopClient.getProducts(page, pageSize);
      log.info("Response for page {}: {}", page, responses);

      if (Objects.isNull(responses) || Objects.isNull(responses.getMessage())) {
        throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
      }

      List<ExternalProductResponse.ExternalResponse> contents = responses.getMessage()
          .getContents();

      if (Objects.isNull(contents) || contents.isEmpty()) {
        break;
      }

      Category category = categoryRepository.findById(1L)
          .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

      List<Product> products = new ArrayList<>();
      for (ExternalProductResponse.ExternalResponse externalProduct : contents) {
        Product product = Product.builder()
            .name(externalProduct.getName())
            .description(externalProduct.getDescription())
            .stock(externalProduct.getStock())
            .price(externalProduct.getPrice())
            .category(category)
            .build();
        products.add(product);
      }
      productRepository.saveAll(products);

      ExternalProductResponse.ExternalPageable pageable = responses.getMessage().getPageable();
      if (Objects.nonNull(pageable)) {
        lastPage = pageable.isLast();
      } else {
        lastPage = contents.size() < pageSize;
      }
      page++;
    }
  }

//  public void saveAllExternalProducts() {
//    int page = 0;
//    int pageSize = 10;
//    boolean lastPage = false;
//
//    while (!lastPage) {
//      processPage(page, pageSize); // 실패하면 이 페이지만 retry
//
//      ExternalProductResponse responses = externalShopClient.getProducts(page, pageSize);
//      ExternalProductResponse.ExternalPageable pageable = responses.getMessage().getPageable();
//
//      lastPage = pageable != null ? pageable.isLast()
//          : responses.getMessage().getContents().size() < pageSize;
//
//      page++;
//    }
//  }

//
//  @Transactional
//  @Retryable(value = ServiceException.class, maxAttempts = 10, backoff = @Backoff(delay = 1000), listeners = {
//      "loggingRetryListener"})
//  public void processPage(int page, int pageSize) {
//    ExternalProductResponse responses = externalShopClient.getProducts(page, pageSize);
//    if (Objects.isNull(responses) || Objects.isNull(responses.getMessage())) {
//      throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
//    }
//
//    List<ExternalProductResponse.ExternalResponse> contents = responses.getMessage().getContents();
//    if (Objects.isNull(contents) || contents.isEmpty()) {
//      return;
//    }
//
//    Category category = categoryRepository.findById(1L)
//        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
//
//    List<Product> products = contents.stream().map(externalProduct ->
//        Product.builder()
//            .name(externalProduct.getName())
//            .description(externalProduct.getDescription())
//            .stock(externalProduct.getStock())
//            .price(externalProduct.getPrice())
//            .category(category)
//            .build()
//    ).toList();
//
//    productRepository.saveAll(products);
//  }

}
