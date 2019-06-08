package com.bookbook.user.api;

import com.bookbook.user.api.dto.CreateUserDto;
import com.bookbook.user.api.validation.CreateUserValidator;
import com.bookbook.user.domain.User;
import com.bookbook.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @InitBinder("createUserDto")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(createUserValidator);
  }

  @PostMapping("/signUp")
  public void signUp(@RequestBody @Valid CreateUserDto createUserDto) {
    userService.signUp(createUserDto);
  }

  @GetMapping(value = "/new/{token}")
  @PreAuthorize("@userCache.getIfPresent(#token) != null")
  public void create(@PathVariable String token) {
    userService.create(token);
  }

  @GetMapping(params = "login")
  public User getUser(@RequestParam String login) {
    return userService.getByLogin(login);
  }

}
