package com.bookbook.user.api;

import com.bookbook.user.api.validation.CreateUserValidator;
import com.bookbook.user.domain.NewUser;
import com.bookbook.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

  @InitBinder("newUser")
  public void initBinder(WebDataBinder binder) {
    binder.addValidators(createUserValidator);
  }

  @PostMapping("/signUp")
  public void signUp(@RequestBody @Valid NewUser newUser) {
    userService.signUp(newUser);
  }

  @GetMapping(value = "/new", params = "token")
  public ResponseEntity create(@RequestParam String token) {
    String html = userService.create(token);
    return ResponseEntity.ok(html);
  }

}
