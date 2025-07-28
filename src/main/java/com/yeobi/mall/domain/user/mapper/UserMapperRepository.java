package com.yeobi.mall.domain.user.mapper;

import com.yeobi.mall.domain.user.dto.SearchUserDto;
import com.yeobi.mall.domain.user.dto.UserDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

//<!--  일단 임시로 UserMapperRepository 라고 적음-->
//<!--  Mybatis 에서는 보통 DB 와 소통하는 네이밍은 ~Mapper 가 정석임-->
//<!--  지금은 UserMapper 를 사용중이므로 규칙을 임시로 만들었음.-->
@Mapper
public interface UserMapperRepository {

  SearchUserDto getUserById(Long id);

//  // 단일건
//  void insertUser(UserDto user);

  // 단일건
  void insertUser(@Param("users") List<UserDto> users);

  void updateUser(UserDto user);

  void deleteUser(UserDto user);
}
