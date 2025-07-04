package com.sparta.java_02.domain.purchase.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.java_02.domain.purchase.dto.PurchaseProductRequestTest;
import com.sparta.java_02.domain.purchase.dto.PurchaseRequestTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void 주문_생성() throws Exception {
    // given: 테스트에 사용할 요청 DTO와 JSON Body 준비
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    PurchaseProductRequestTest purchaseProductRequestTest = new PurchaseProductRequestTest(1L, 10);
    purchaseProductRequestTests.add(purchaseProductRequestTest);

    PurchaseRequestTest request = new PurchaseRequestTest(1L, purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(request);

    // when & then: API를 호출하고 응답을 검증
    mockMvc.perform(MockMvcRequestBuilders.post(
                "/api/purchases")               // 1. HTTP POST 요청을 /api/purchases 로 보냄
            .contentType(MediaType.APPLICATION_JSON.toString())    // 2. 요청의 Content-Type을 JSON으로 설정
            .content(requestBody)                                  // 3. 요청 Body에 JSON 데이터 추가
            .accept(MediaType.APPLICATION_JSON.toString()))        // 4. 클라이언트가 JSON 응답을 기대함을 명시
            .andExpect(status().isOk())                       // 5. 응답 상태 코드가 200 Created 인지 검증
            .andExpect(MockMvcResultMatchers.jsonPath("$.result")
            .value(true));    // 6. 응답 Body의 result 필드가 true인지 검증
  }
  @Test
  void 유저_없음_체크() throws Exception {
    // given : 데이터 주는거 무엇
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    PurchaseProductRequestTest purchaseProductRequestTest = new PurchaseProductRequestTest(1L, 10);
    purchaseProductRequestTests.add(purchaseProductRequestTest);

    PurchaseRequestTest request = new PurchaseRequestTest(10L, purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(purchaseProductRequestTest);

    // when : 언제 실행하냐
    // when & then: API를 호출하고 응답을 검증
    mockMvc.perform(post(
                "/api/purchases")               // 1. HTTP POST 요청을 /api/purchases 로 보냄
            .contentType(MediaType.APPLICATION_JSON.toString())    // 2. 요청의 Content-Type을 JSON으로 설정
            .content(requestBody)                                  // 3. 요청 Body에 JSON 데이터 추가
            .accept(MediaType.APPLICATION_JSON.toString()))        // 4. 클라이언트가 JSON 응답을 기대함을 명시
        .andExpect(status()
            .isOk())                           // 5. 응답 상태 코드가 200 Created 인지 검증
        .andExpect(jsonPath("$.error.errorCode")
            .value("NOT_FOUND_USER"));    // 6. 응답 Body의 result 필드가 true인지 검증
  }

  @Test
  void 수량_체크() throws Exception {
    // given : 데이터 주는거 무엇
    List<PurchaseProductRequestTest> purchaseProductRequestTests = new ArrayList<>();
    PurchaseProductRequestTest purchaseProductRequestTest = new PurchaseProductRequestTest(1L, 10);
    purchaseProductRequestTests.add(purchaseProductRequestTest);

    PurchaseRequestTest request = new PurchaseRequestTest(1L, purchaseProductRequestTests);

    String requestBody = new ObjectMapper().writeValueAsString(purchaseProductRequestTest);

    // when : 언제 실행하냐
    // when & then: API를 호출하고 응답을 검증
    mockMvc.perform(post(
                "/api/purchases")               // 1. HTTP POST 요청을 /api/purchases 로 보냄
            .contentType(MediaType.APPLICATION_JSON.toString())    // 2. 요청의 Content-Type을 JSON으로 설정
            .content(requestBody)                                  // 3. 요청 Body에 JSON 데이터 추가
            .accept(MediaType.APPLICATION_JSON.toString()))        // 4. 클라이언트가 JSON 응답을 기대함을 명시
        .andExpect(status()
            .isOk())                           // 5. 응답 상태 코드가 200 Created 인지 검증
        .andExpect(jsonPath("$.error.errorCode")
            .value("OUT_OF_STOCK_PRODUCT"));    // 6. 응답 Body의 result 필드가 true인지 검증
  }
}