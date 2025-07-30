package com.yeobi.mall.global.external.client;

import com.yeobi.mall.global.config.OpenFeignConfig;
import com.yeobi.mall.global.external.dto.ExternalProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "external-shop",
    url = "${external.external-shop.url}",
    configuration = OpenFeignConfig.class
)

public interface ExternalShopClient {

  // 외부 API Call 하는곳
  @GetMapping("/products")
  ExternalProductResponse getProducts(@RequestParam("page") Integer page,
      @RequestParam("size") Integer size);
}

