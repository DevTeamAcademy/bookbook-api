package com.bookbook.general.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.bookbook.general.constant.SpringProfiles.NOT_TEST_PROFILE;

@Component
@Profile(NOT_TEST_PROFILE)
public class UserInfoInitializer {
  private ResourceServerTokenServices resourceServerTokenServices;

  @Autowired
  public UserInfoInitializer(ResourceServerTokenServices resourceServerTokenServices) {
    this.resourceServerTokenServices = resourceServerTokenServices;
  }

  @PostConstruct
  public void initialize() {
    UserInfo.setResourceServerTokenServices(resourceServerTokenServices);
  }
}