package com.bookbook.user.api.validation;

import com.bookbook.user.domain.NewUser;
import com.bookbook.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CreateUserValidator implements Validator {

  @Autowired
  private UserService userService;

  @Override
  public boolean supports(Class<?> aClass) {
    return NewUser.class.equals(aClass);
  }

  @Override
  public void validate(Object object, Errors errors) {
    NewUser newUser = (NewUser) object;
    if (userService.existsByEmail(newUser.getEmail())) {
      errors.rejectValue("email", "validation.newUser.email.nonUnique", new String[]{newUser.getEmail()},
          "User with email " + newUser.getEmail() + " already exists.");
    }
    if (userService.existsByLogin(newUser.getLogin())) {
      errors.rejectValue("login", "validation.newUser.loginId.nonUnique", new String[]{newUser.getLogin()},
          "User with login " + newUser.getLogin() + " already exists.");
    }
  }

}
