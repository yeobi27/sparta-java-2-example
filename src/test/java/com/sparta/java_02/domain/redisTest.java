package com.sparta.java_02.domain;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
public class redisTest {

  private static final Logger log = LoggerFactory.getLogger(redisTest.class);

  @Autowired
  private Jedis jedis;

  @Test
  void testJedis() {
    // set, get
    jedis.set("key", "Hello Jedis!!");
    String value = jedis.get("key");

    log.info("jedisValue: {}", value);
  }
}
