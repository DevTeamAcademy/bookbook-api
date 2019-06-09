package com.bookbook.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UserPasswordTokenPurgeTask {

  @Autowired
  private UserService userService;

  @Scheduled(cron = "${user.purge.cron}")
  public void purgeExpired() {
    userService.deleteAllExpiration();
  }

}