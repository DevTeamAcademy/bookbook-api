package com.bookbook.user.service;

import com.bookbook.general.service.AbstractPersistenceService;
import com.bookbook.user.api.dto.SingIn;
import com.bookbook.user.domain.User;
import com.bookbook.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService extends AbstractPersistenceService<User> {

  @Autowired
  private UserRepository repository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public UserService() {
    super(User.class);
  }

  @Override
  protected JpaRepository<User, String> getRepository() {
    return repository;
  }

  @Transactional
  public void singIn(SingIn singIn) {
    User user = new User();
    user.setLogin(singIn.getLogin());
    user.setPassword(passwordEncoder.encode(singIn.getPassword()));
    create(user);
  }

}
