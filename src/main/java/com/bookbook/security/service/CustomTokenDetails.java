package com.bookbook.security.service;

public class CustomTokenDetails {
  private final String userGuid;

  public CustomTokenDetails(String userGuid) {
    this.userGuid = userGuid;
  }

  public String getUserGuid() {
    return userGuid;
  }

}