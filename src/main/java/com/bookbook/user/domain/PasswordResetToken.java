package com.bookbook.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_PASSWORD_RESET")
public class PasswordResetToken {
  @Column(nullable = false, name = "USER_GUID")
  private String userGuid;
  @Column(name = "TOKEN")
  private String token;
  @Column(name = "EXPIRATION_DATE")
  private LocalDateTime expiration;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUserGuid() {
    return userGuid;
  }

  public void setUserGuid(String userGuid) {
    this.userGuid = userGuid;
  }

  public LocalDateTime getExpiration() {
    return expiration;
  }

  public void setExpiration(LocalDateTime expiration) {
    this.expiration = expiration;
  }
}