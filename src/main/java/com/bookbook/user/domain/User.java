package com.bookbook.user.domain;

import com.bookbook.general.domain.AuditedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USER_PROFILE")
public class User extends AuditedEntity {

  @Column(name = "LOGIN")
  private String login;
  @Column(name = "PASSWORD")
  private String password;
  @Column(name = "MAIL")
  private String mail;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }
}
