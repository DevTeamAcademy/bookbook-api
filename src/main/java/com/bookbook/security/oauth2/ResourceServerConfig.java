package com.bookbook.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

//  @Autowired
//  private ApplicationContext applicationContext;
//  @Autowired(required = false)
//  private SocketOAuth2AuthenticationManager oAuth2AuthenticationManager;

//  @Override
//  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//    if (Objects.nonNull(oAuth2AuthenticationManager)) {
//      resources.authenticationManager(oAuth2AuthenticationManager);
//    }
//    super.configure(resources);
//  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
//    String[] ignoredSwaggerUrls = applicationContext.getEnvironment().acceptsProfiles(Profiles.of(DEVELOPMENT_PROFILE, LOCAL_PROFILE)) ? SWAGGER_RESOURCES : new String[]{};
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.cors().and().authorizeRequests()
//        .antMatchers(ignoredSwaggerUrls).permitAll()
        .antMatchers(HttpMethod.GET, "/actuator/health").permitAll()
        .antMatchers(HttpMethod.GET, "/websocket", "/websocket/**").permitAll()
        .anyRequest().authenticated();
  }
}
