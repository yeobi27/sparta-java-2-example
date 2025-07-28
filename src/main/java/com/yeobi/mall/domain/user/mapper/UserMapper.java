package com.yeobi.mall.domain.user.mapper;

import com.yeobi.mall.domain.user.dto.UserCreateRequest;
import com.yeobi.mall.domain.user.dto.UserResponse;
import com.yeobi.mall.domain.user.dto.UserSearchResponse;
import com.yeobi.mall.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  // source -> Users , target -> UserResponse
  // User 의 email 필드는 UserResponse 의 userEmail 필드이다.
//  @Mapping(target = "userEmail", source = "email")
  UserResponse toResponse(User user);

  UserSearchResponse toSearch(User user);

  User toEntity(UserCreateRequest request);
}
