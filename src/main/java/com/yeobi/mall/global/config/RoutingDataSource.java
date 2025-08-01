package com.yeobi.mall.global.config;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

  @NotNull
  @Override
  protected Object determineCurrentLookupKey() {
    String threadLocalKey = DataSourceContextHolder.getDataSourceKey();
    if (threadLocalKey != null) {
      log.info("Using ThreadLocal datasource key: {}", threadLocalKey);
      return threadLocalKey;
    }

    /*
    * DataSourceConfig 클래스에서 작성한 Map 안에 Key 값을 전달해준다.
    Map<Object, Object> datasourceMap = new HashMap<>();
    datasourceMap.put("master", masterDataSource);
    datasourceMap.put("slave", slaveDataSource);
    */

    // 트랜잭션이 read 인지 write 인지 구분해준다.
    boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    String key = isReadOnly ? "slave" : "master"; // Key 값 전달하기
    log.info("Transaction ReadOnly: {}, Using: {}", isReadOnly, key);
    return key;
  }
}
