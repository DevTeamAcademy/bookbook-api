package com.bookbook.security;

import com.bookbook.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
//@Profile(NOT_TEST_PROFILE)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  public void globalUserDetails(final AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    String[] ignoredSwaggerUrls = applicationContext.getEnvironment().acceptsProfiles(DEVELOPMENT_PROFILE, LOCAL_PROFILE) ? SWAGGER_RESOURCES : new String[]{};
    http.authorizeRequests()
//        .antMatchers(ignoredSwaggerUrls).permitAll()
        .antMatchers(HttpMethod.GET, "/health").permitAll()
//        .antMatchers("/loggers", "/loggers/**").hasRole(LIUBANCHYK_ROLE)
        .anyRequest().authenticated();
  }
}