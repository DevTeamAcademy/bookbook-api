package com.bookbook.user.repository;

import com.bookbook.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

  public Optional<User> getUserDetails(String username) {
    return Optional.empty();
  }

}
