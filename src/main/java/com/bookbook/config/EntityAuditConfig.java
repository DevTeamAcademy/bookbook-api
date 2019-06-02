package com.bookbook.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZonedDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "auditingDateTimeProvider")
public class EntityAuditConfig {

  public class AuditorProviderAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String auditor = authentication != null ? authentication.getPrincipal().toString() : "system";
      return Optional.of(auditor);
    }

  }

  @Bean
  public AuditorProviderAware auditorProvider() {
    return new AuditorProviderAware();
  }

  @Bean(name = "auditingDateTimeProvider")
  public DateTimeProvider dateTimeProvider() {
    return () -> Optional.of(ZonedDateTime.now());
  }
}
