package com.bookbook.user.config;

import com.bookbook.user.api.dto.CreateUserDto;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class UserConfig {

  @Value("${user.signUpExpiration}")
  private Duration signUpExpiration;

  @Bean
  Cache<String, CreateUserDto> userCache() {
    return CacheBuilder.newBuilder()
        .expireAfterWrite(signUpExpiration.toHours(), TimeUnit.HOURS)
        .build();
  }

}