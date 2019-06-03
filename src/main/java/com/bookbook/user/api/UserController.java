package com.bookbook.user.api;

import com.bookbook.user.api.dto.SingIn;
import com.bookbook.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  public void singIn(@RequestBody SingIn singIn) {
    userService.singIn(singIn);
  }

  @GetMapping
  public String singIn() {
    return "fsdfds";
  }

}
