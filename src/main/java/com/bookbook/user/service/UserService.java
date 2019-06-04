package com.bookbook.user.service;

import com.bookbook.general.service.AbstractPersistenceService;
import com.bookbook.mail.dto.Mail;
import com.bookbook.mail.service.MailService;
import com.bookbook.user.api.dto.SingInDto;
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
  @Autowired
  private MailService mailService;

  public UserService() {
    super(User.class);
  }

  @Override
  protected JpaRepository<User, String> getRepository() {
    return repository;
  }

  @Transactional
  public void singIn(SingInDto singInDto) {
    Mail mail = new Mail()
        .setFrom("book.book.dev@gmail.com")
        .setTo("ploskiy.andriy@gmail.com")
        .setText("fdsfsdfasdfdsaa");
    mailService.sendMail(mail);

    User user = new User();
    user.setLogin(singInDto.getLogin());
    user.setPassword(passwordEncoder.encode(singInDto.getPassword()));
    user.setEmail(singInDto.getEmail());
    super.create(user);
  }

  public boolean existsByEmail(String email) {
    return repository.existsByEmail(email);
  }

  public boolean existsByLogin(String login) {
    return repository.existsByLogin(login);
  }

  public User getByLogin(String login) {
    return repository.findOneByLogin(login).orElse(null);
  }
}
