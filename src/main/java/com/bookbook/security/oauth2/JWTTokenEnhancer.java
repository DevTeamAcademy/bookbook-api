package com.bookbook.security.oauth2;

import com.bookbook.security.service.Oauth2UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenEnhancer implements TokenEnhancer {
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

    Object principal = authentication.getPrincipal();

    Map<String, Object> additionalInfo = new HashMap<>();
//    if (principal instanceof Oauth2UserDetails) {
    Oauth2UserDetails oauth2UserDetails = (Oauth2UserDetails) principal;
    additionalInfo.put("role", ((Oauth2UserDetails) principal).getRole());
//    }
// else {
//      CustomTokenDetails details = (CustomTokenDetails) authentication.getDetails();
//      additionalInfo.put(USER_GUID, details.getUserGuid());
//      additionalInfo.put(ENTERPRISE_GUID, details.getEnterpriseGuid());
//    }
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    return accessToken;
  }
}