package com.bookbook.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class Oauth2UserDetails implements UserDetails {
  private final String login;
  private final String password;
  private final String userGuid;
  private final Set<SimpleGrantedAuthority> authorities;

  public Oauth2UserDetails(String login, String password, String userGuid, Set<SimpleGrantedAuthority> authorities) {
    this.login = login;
    this.password = password;
    this.userGuid = userGuid;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return login;
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

  @Override
  public String toString() {
    return login;
  }

  public String getRole() {
    return authorities.stream().findFirst().map(SimpleGrantedAuthority::getAuthority).orElseThrow(RuntimeException::new);
  }

  public String getUserGuid() {
    return userGuid;
  }
}