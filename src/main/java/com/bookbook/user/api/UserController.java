package com.bookbook.user.api;

import com.bookbook.user.api.validation.CreateUserValidator;
import com.bookbook.user.domain.NewUser;
import com.bookbook.user.service.PasswordService;
import com.bookbook.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user")
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private PasswordService passwordService;
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

  @PutMapping("/updatePassword")
  public void updatePassword(String newPassword) {
    userService.changePassword(newPassword);
  }

  @PostMapping("/forgot")
  public void forgotPassword(@RequestParam String loginOrEmail) {
    passwordService.forgotPassword(loginOrEmail);
  }

  @GetMapping("/changePassword")
  public RedirectView changePassword(String token) {
    String url = passwordService.changePassword(token);
    return new RedirectView(url);
  }

  @PutMapping("/updatePasswordFromForgot")
  public void changePasswordFromForgot(String newPassword) {
    userService.changePassword(newPassword);
  }

}
