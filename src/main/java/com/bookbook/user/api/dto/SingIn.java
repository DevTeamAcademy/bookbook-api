package com.bookbook.user.api.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class SingIn {

  @NotBlank
  @Length(min = 4, max = 255)
  private String login;
  @NotBlank
  @Length(min = 4, max = 255)
  private String password;

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
}
