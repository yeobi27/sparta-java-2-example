package com.sparta.java_02.domain.product.controller;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.response.ApiResponse;
import com.sparta.java_02.domain.product.dto.ProductRequest;
import com.sparta.java_02.domain.product.dto.ProductResponse;
import com.sparta.java_02.domain.product.dto.ProductUpdateRequest;
import com.sparta.java_02.domain.product.entity.Product;
import com.sparta.java_02.domain.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import javax.sql.rowset.serial.SerialException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
  /*@RestController는 내부적으로 @ResponseBody를 포함.
  @RestController Ctrl+클릭해서 들어가보면 볼수있음
  그래서 List<Product>를 반환하면,
  Spring이 자동으로 Jackson 라이브러리를 이용해 JSON 배열로
  직렬화(serialize)함.*/
  private final ProductService productService; // Service 계층의 의존성을 주입받음

  @GetMapping("/search")
  public List<ProductResponse> searchProducts(@RequestParam String keyword) {
    return productService.searchByName(keyword);
  }

  // 1. 등록: DTO 사용 + 유효성 검증
  @PostMapping
  public ProductResponse create(@RequestBody @Valid ProductRequest request){
    return productService.create(request);
  }
//  // 2. 전체목록 조회
//  @GetMapping
//  public List<ProductResponse> getAll(){
//    return productService.getAll();
//  }

  @GetMapping
  public ApiResponse<List<ProductResponse>> getAll(){
    return ApiResponse.success(productService.getAll());
  }

  //// 2-1. 단일항목 조회 : 변화 3단계 일반 DTO 반환 -> ResponseEntity -> ApiResponse<T> 반환
//  @GetMapping("/{id}")
//  public ProductResponse getById(@PathVariable Long id){
//    return productService.getById(id);
//  }

//  @GetMapping("/{id}")
//  public ResponseEntity<ProductResponse> getById(@PathVariable Long id){
//    try{
//      ProductResponse response = productService.getById(id);
//      return ResponseEntity.ok(response); // 200 OK
//    } catch (ServiceException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품을 찾을 수 없습니다.");
//    }
//  }

  @GetMapping("/{id}")
  public ApiResponse<ProductResponse> getById(@PathVariable Long id){
    return ApiResponse.success(productService.getById(id));
  }

//  // 3. 수정 - PUT
//  @PutMapping("/{id}")
//  public ProductResponse update(@PathVariable Long id, @RequestBody @Valid ProductUpdateRequest request){
//    return productService.update(id, request);
//  }
  // 3. 수정 - PUT
  @PutMapping("/{id}")
  public ApiResponse<ProductResponse> update(@PathVariable Long id, @RequestBody @Valid ProductUpdateRequest request){
    return ApiResponse.success(productService.update(id, request));
  }

//  @DeleteMapping("/{id}")
//  @ResponseStatus(HttpStatus.NO_CONTENT)  // 정상적인 200 OK 가 뜨기에는 삭제는 204 상태 코드가 적합하다.
//  public void delete(@PathVariable Long id){
//    productService.delete(id);
//  }
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)  // 정상적인 200 OK 가 뜨기에는 삭제는 204 상태 코드가 적합하다.
  public ApiResponse<Void> delete(@PathVariable Long id){
    productService.delete(id);
    return ApiResponse.success();
  }
}
