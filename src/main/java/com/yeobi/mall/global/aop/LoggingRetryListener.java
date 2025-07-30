package com.yeobi.mall.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingRetryListener implements RetryListener {

  @Override
  public <T, E extends Throwable> boolean open(RetryContext context,
      RetryCallback<T, E> callback) {
    return true;
  }

  @Override
  public <T, E extends Throwable> void close(RetryContext context,
      RetryCallback<T, E> callback,
      Throwable throwable) {
    // 재시도 종료 시 호출됨 (성공/실패 관계 없이)
  }

  @Override
  public <T, E extends Throwable> void onError(RetryContext context,
      RetryCallback<T, E> callback,
      Throwable throwable) {
    log.warn("Retry: attempt {}", context.getRetryCount() + 1); // 첫 번째는 0부터 시작
  }
}
