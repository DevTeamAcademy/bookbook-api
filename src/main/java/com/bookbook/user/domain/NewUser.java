package com.bookbook.user.domain;

import com.bookbook.general.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_NEW")
public class NewUser extends BaseEntity {
  @NotBlank
  @Length(min = 4, max = 255)
  private String login;
  @NotBlank
  @Length(min = 4, max = 255)
  private String password;
  @NotBlank
  private String email;
  @JsonIgnore
  @Column(name = "EXPIRATION_DATE")
  private LocalDateTime expiration;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getExpiration() {
    return expiration;
  }

  public void setExpiration(LocalDateTime expiration) {
    this.expiration = expiration;
  }
}