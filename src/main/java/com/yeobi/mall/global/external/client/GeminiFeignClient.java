package com.yeobi.mall.global.external.client;

import com.yeobi.mall.global.config.OpenFeignConfig;
import com.yeobi.mall.global.external.dto.GeminiDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

// name: Feign 클라이언트의 고유 이름, url: application.yml에서 설정한 URL을 참조
@FeignClient(
    name = "gemini-api",
    url = "${gemini.api.url}",
    configuration = OpenFeignConfig.class
)
public interface GeminiFeignClient {

  @PostMapping(value = "/v1beta/models/gemini-2.0-flash:generateContent")
  GeminiDto.Response generateContent(
      @RequestParam("key") String apiKey,
      @RequestBody GeminiDto.Request request
  );
}