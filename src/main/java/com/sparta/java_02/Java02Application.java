package com.sparta.java_02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Java02Application {

  public static void main(String[] args) {
    SpringApplication.run(Java02Application.class, args);

//    User user = User.builder()
//        .name("이름")
//        .email("이메일")
//        //.passwordHash("패스워드")  // null 이라면 빼버리면됌, 생성자라면 null 넣어줘야함.
//        .build();
  }

}
