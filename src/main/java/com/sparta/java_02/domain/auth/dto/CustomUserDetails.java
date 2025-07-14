package com.sparta.java_02.domain.auth.dto;

import com.sparta.java_02.domain.user.entity.User;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
public class CustomUserDetails implements UserDetails{

  private final User user;

  public CustomUserDetails(User user) {
    this.user = user;
  }

  // 사용자 권한을 반환한다.
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // 필요시 권한 확장
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
  }

  // Hash된 비밀번호를 반환
  @Override
  public String getPassword() {
    return user.getPasswordHash();
  }

  // 유저의 Email 을 반환
  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  // 유저객체 반환
  public User getUser() {
    return user;
  }
}
