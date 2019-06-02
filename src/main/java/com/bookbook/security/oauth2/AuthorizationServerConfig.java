package com.bookbook.security.oauth2;

import com.bookbook.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  @Value("${security.oauth2.resource.jwt.accessTokenValiditySeconds}")
  private int accessTokenValiditySeconds;
  @Value("${security.oauth2.resource.jwt.refreshTokenValiditySeconds}")
  private int refreshTokenValiditySeconds;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;
  @Autowired
  private AuthorizationServerTokenServices defaultTokenServices;
  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    oauthServer
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()");
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient("frontend")
        .secret("$2a$10$E5JyVIXFBT2aXw/jA/Wvf.S8ERfQiU0Rvu1tcPRXJVigPt/ubZ5tm")
        .authorizedGrantTypes("password", "refresh_token")
        .scopes("read", "write")
        .accessTokenValiditySeconds(accessTokenValiditySeconds)
        .refreshTokenValiditySeconds(refreshTokenValiditySeconds);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
        .tokenServices(defaultTokenServices)
        .userDetailsService(userDetailsService)
        .authenticationManager(authenticationManager);
  }

}