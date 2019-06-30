package com.bookbook.general.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Map;
import java.util.Objects;

import static com.bookbook.security.constant.SecurityConstants.ROLE_PREFIX;
import static com.bookbook.security.constant.TokenKeys.LOGIN;
import static com.bookbook.security.constant.TokenKeys.USER_GUID;

public class UserInfo {

  private static ResourceServerTokenServices resourceServerTokenServices;

  private UserInfo() {
  }

  public static String getLoginId() {
    return getProperty(LOGIN);
  }

  public static String getUserGuid() {
    return getProperty(USER_GUID);
  }

  private static String getProperty(String propertyName) {
    String tokenValue = getToken();
    if (Objects.isNull(tokenValue)) {
      return null;
    }
    Map<String, Object> additionalInformation = resourceServerTokenServices.readAccessToken(tokenValue).getAdditionalInformation();
    Object result = additionalInformation.get(propertyName);
    if (result == null) {
      throw new IllegalArgumentException("Invalid OAuth2 Token details key: " + propertyName);
    }

    return String.valueOf(additionalInformation.get(propertyName));
  }

  private static String getToken() {
    OAuth2Authentication authentication = getAuthentication();
    if (authentication == null) {
      return null;
    }
    OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
    return oAuth2AuthenticationDetails.getTokenValue();
  }

  public static boolean hasRole(String roleName) {
    String authorityle = ROLE_PREFIX + roleName;
    return getAuthentication().getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(authorityle::equals);
  }

  private static OAuth2Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2Authentication) {
      return (OAuth2Authentication) authentication;
    }
    return null;
  }

  static void setResourceServerTokenServices(ResourceServerTokenServices resourceServerTokenServices) {
    UserInfo.resourceServerTokenServices = resourceServerTokenServices;
  }


}