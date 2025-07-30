package com.yeobi.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

// 내부적으로 비동기를 쓰려면 꼭 추가해줘야하는 @EnableAsync
// RedisHttpSession 허용
// @EnableFeignClients 외부 API 콜을 위해서 선언해뒀음
@EnableAsync
@EnableFeignClients
@EnableRetry
@EnableScheduling // 스케줄링 기능을 활성화합니다.
@EnableRedisHttpSession
@SpringBootApplication
public class Java02Application {

  public static void main(String[] args) {
    SpringApplication.run(Java02Application.class, args);
  }

}
