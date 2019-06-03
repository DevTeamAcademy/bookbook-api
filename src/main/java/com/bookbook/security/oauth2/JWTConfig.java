package com.bookbook.security.oauth2;

import com.bookbook.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;

@Configuration
public class JWTConfig {
  @Value("${security.oauth2.jwt.keypass}")
  private String keypass;
  @Value("${security.oauth2.jwt.keystore}")
  private String keystore;
  @Value("${security.oauth2.jwt.keyStorePath}")
  private String keyStorePath;

  private final ClientDetailsService clientDetailsService;
  private final UserDetailsServiceImpl userDetailsService;

  @Autowired
  public JWTConfig(ClientDetailsService clientDetailsService,
                   UserDetailsServiceImpl userDetailsService) {
    this.clientDetailsService = clientDetailsService;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public JwtTokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setAccessTokenConverter(defaultAccessTokenConverter());
    KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyStorePath), keypass.toCharArray());
    jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair(keystore));
    return jwtAccessTokenConverter;
  }

  @Primary
  @Bean
  public AuthorizationServerTokenServices defaultTokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setClientDetailsService(clientDetailsService);
    defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());
    defaultTokenServices.setSupportRefreshToken(true);

    return defaultTokenServices;
  }

  @Bean
  public DefaultAccessTokenConverter defaultAccessTokenConverter() {
    DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
    DefaultUserAuthenticationConverter userConverter = new DefaultUserAuthenticationConverter();
    userConverter.setUserDetailsService(userDetailsService);
    converter.setUserTokenConverter(userConverter);

    return converter;
  }

  @Bean
  public TokenEnhancerChain tokenEnhancerChain() {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
    return tokenEnhancerChain;
  }

  @Bean
  public TokenEnhancer tokenEnhancer() {
    return new JWTTokenEnhancer();
  }

}