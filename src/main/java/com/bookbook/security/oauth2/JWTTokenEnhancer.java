package com.bookbook.security.oauth2;

import com.bookbook.security.service.Oauth2UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.bookbook.security.constant.TokenKeys.ROLE;
import static com.bookbook.security.constant.TokenKeys.USER_GUID;

@Component
public class JWTTokenEnhancer implements TokenEnhancer {
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    Object principal = authentication.getPrincipal();

    Oauth2UserDetails oauth2UserDetails = (Oauth2UserDetails) principal;
    Map<String, Object> additionalInfo = new HashMap<>();
    additionalInfo.put(ROLE, oauth2UserDetails.getRole());
    additionalInfo.put(USER_GUID, oauth2UserDetails.getUserGuid());

    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    return accessToken;
  }
}