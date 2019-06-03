package com.bookbook.user.api.validation;

import com.bookbook.user.api.dto.SingInDto;
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
    return SingInDto.class.equals(aClass);
  }

  @Override
  public void validate(Object object, Errors errors) {
    SingInDto user = (SingInDto) object;
    if (userService.existsByEmail(user.getEmail())) {
      errors.rejectValue("email", "validation.user.email.nonUnique", new String[]{user.getEmail()},
          "User with email " + user.getEmail() + " already exists.");
    }
    if (userService.existsByLogin(user.getLogin())) {
      errors.rejectValue("login", "validation.user.loginId.nonUnique", new String[]{user.getLogin()},
          "User with login " + user.getLogin() + " already exists.");
    }
  }

}
