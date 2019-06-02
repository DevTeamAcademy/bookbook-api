package com.bookbook.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

@Component
public class UserPermissionEvaluator implements PermissionEvaluator {

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
    throw new NotImplementedException();
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable guid, String guidType, Object permission) {
    return true;
  }

}