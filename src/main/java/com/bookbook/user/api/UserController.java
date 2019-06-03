package com.bookbook.user.api;

import com.bookbook.user.api.dto.SingInDto;
import com.bookbook.user.api.validation.CreateUserValidator;
import com.bookbook.user.domain.User;
import com.bookbook.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user")
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private CreateUserValidator createUserValidator;

  @InitBinder("singInDto")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(createUserValidator);
  }

  @PostMapping
  public void singIn(@RequestBody @Valid SingInDto singInDto) {
    userService.singIn(singInDto);
  }

  @GetMapping(params = "login")
  public User getUser(@RequestParam String login) {
    return userService.getByLogin(login);
  }

}
