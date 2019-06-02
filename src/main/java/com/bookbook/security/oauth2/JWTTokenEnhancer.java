package com.bookbook.security.oauth2;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenEnhancer implements TokenEnhancer {
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

//    Object principal = authentication.getPrincipal();

//    Map<String, Object> additionalInfo = new HashMap<>();
//    if (principal instanceof User) {
//      User amousUserDetails = (User) principal;
//      additionalInfo.put(ENTERPRISE_GUID, amousUserDetails.getEnterpriseGuid());
//      additionalInfo.put(USER_GUID, amousUserDetails.getUserGuid());
//      additionalInfo.put(LOGIN_ID, amousUserDetails.getUsername());
//      additionalInfo.put(AUTHORITIES, amousUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
//          .collect(Collectors.toList()));
//      additionalInfo.put(USER_TYPE, amousUserDetails.getUserType());
//    } else {
//      CustomTokenDetails details = (CustomTokenDetails) authentication.getDetails();
//      additionalInfo.put(USER_GUID, details.getUserGuid());
//      additionalInfo.put(ENTERPRISE_GUID, details.getEnterpriseGuid());
//    }
//    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    return accessToken;
  }
}