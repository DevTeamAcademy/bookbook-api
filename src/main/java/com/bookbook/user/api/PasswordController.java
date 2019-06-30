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

  @GetMapping(value = "/reset", params = "token")
  public RedirectView reset(@RequestParam String token) {
    String url = passwordService.reset(token);
    return new RedirectView(url);
  }

  @PutMapping(value = "/change", params = "newPassword")
  public void updatePassword(@RequestParam String newPassword) {
    passwordService.change(newPassword);
  }

  @PutMapping(value = "/changeFromReset", params = "newPassword")
  public void changePasswordFromForgot(@RequestParam String newPassword) {
    passwordService.change(newPassword);
  }

}
