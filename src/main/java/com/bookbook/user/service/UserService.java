package com.bookbook.user.service;

import com.bookbook.general.service.AbstractPersistenceService;
import com.bookbook.user.domain.User;
import com.bookbook.user.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractPersistenceService<User> {

  private UserRepository repository;

  public UserService() {
    super(User.class);
  }

  @Override
  protected JpaRepository<User, String> getRepository() {
    return repository;
  }
}
