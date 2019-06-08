package com.bookbook.general.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {

  @Bean
  protected MethodSecurityExpressionHandler methodSecurityExpressionHandler(PermissionEvaluator permissionEvaluator) {
    OAuth2MethodSecurityExpressionHandler securityExpressionHandler = new OAuth2MethodSecurityExpressionHandler();
    securityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
    return securityExpressionHandler;
  }

}