package com.bookbook.user.api;

import com.bookbook.user.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(value = "/password")
public class PasswordController {

  @Autowired
  private PasswordService passwordService;

  @GetMapping("/forgot")
  public void forgotPassword(@RequestParam String loginOrEmail) {
    passwordService.forgot(loginOrEmail);
  }

  @GetMapping("/reset")
  public RedirectView reset(String token) {
    String url = passwordService.reset(token);
    return new RedirectView(url);
  }

  @PutMapping("/change")
  public void updatePassword(String newPassword) {
    passwordService.change(newPassword);
  }

  @PutMapping("/changeFromReset")
  public void changePasswordFromForgot(String newPassword) {
    passwordService.change(newPassword);
  }

}
