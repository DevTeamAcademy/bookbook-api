package com.bookbook.user.api;

import com.bookbook.user.api.dto.SingIn;
import com.bookbook.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  public void singIn(@RequestBody SingIn singIn) {
    userService.singIn(singIn);
  }

}
