package com.yeobi.mall.global.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

  private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

//  /**
//   * fixedRate: 이전 작업 종료와 상관없이 5초마다 시작
//   */
//  @Scheduled(fixedRate = 5000)
//  public void runTaskEvery5Seconds() {
//    log.info("fixedRate 작업 실행 - {}", LocalDateTime.now());
//  }
//
//  /**
//   * cron: 매분 0초에 실행 (테스트용) "0 * * * * *" -> 매분 0초
//   */
//  @Scheduled(cron = "0 * * * * *")
//  public void runTaskAtEveryMinute() {
//    log.info("cron 작업 실행 - {}", LocalDateTime.now());
//  }
//
//  // 매일마다 밤 12시05분에 실행되게한다.
//  @Scheduled(cron = "0 5 12 ? * 0-7")
//  public void batchUser() {
//    log.info("run batchUser : 유저에 대한 배치처리를 합니다.");
//  }
}