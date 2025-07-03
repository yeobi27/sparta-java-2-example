package com.sparta.java_02.domain.purchase.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PurchaseControllerRestAssuredTest {

  @LocalServerPort
  private int port; // DI

  // 테스트 케이스들을 실행할때마다 반복적으로 그 전에! 적용되는것 초기화 세팅
  @BeforeEach
  void setup() {
    RestAssured.port = port;
  }

  @Test
  void 주문_성공() {
    // given
    String requestBody = """        
        {
          "userId" : 1,
          "purchaseProducts" : [
            {
              "productId" : 1,
              "quantity" : 10
            }
          ]
        }                
        """;
    // when & then
    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(requestBody)
        .when()
        .post("/api/purchases")
        .then().log().all()
        .statusCode(200)
        .body("result", IsEqual.equalTo(true));
  }
}
