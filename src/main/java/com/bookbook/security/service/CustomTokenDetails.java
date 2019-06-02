package com.bookbook.security.service;

public class CustomTokenDetails {
  private final String userGuid;
  private final String enterpriseGuid;

  public CustomTokenDetails(String userGuid, String enterpriseGuid) {
    this.userGuid = userGuid;
    this.enterpriseGuid = enterpriseGuid;
  }

  public String getUserGuid() {
    return userGuid;
  }

  public String getEnterpriseGuid() {
    return enterpriseGuid;
  }
}