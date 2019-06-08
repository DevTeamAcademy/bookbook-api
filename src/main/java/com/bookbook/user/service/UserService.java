package com.bookbook.user.service;

import com.bookbook.general.service.AbstractPersistenceService;
import com.bookbook.mail.dto.Mail;
import com.bookbook.mail.service.MailService;
import com.bookbook.template.service.TemplateService;
import com.bookbook.user.api.dto.CreateUserDto;
import com.bookbook.user.domain.User;
import com.bookbook.user.repository.UserRepository;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class UserService extends AbstractPersistenceService<User> {

  @Value("${url.frontEnd}/user/new")
  private String fontEndUrl;
  @Value("${mail.from}")
  private String fromMail;

  @Autowired
  private UserRepository repository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private MailService mailService;
  @Autowired
  private Cache<String, CreateUserDto> userCache;
  @Autowired
  private TemplateService templateService;


  public UserService() {
    super(User.class);
  }

  @Override
  protected JpaRepository<User, String> getRepository() {
    return repository;
  }

  @Transactional
  public void signUp(CreateUserDto createUserDto) {
    String token = UUID.randomUUID().toString();
    String mailVerifyUrl = String.join("/", fontEndUrl, token);

//    HashMap<String, Object> data = Maps.newHashMap();
//    data.put("mailVerifyUrl", mailVerifyUrl);
//    String html = templateService.build(Templates.SING_UP, data);

    Mail mail = new Mail()
        .setFrom(fromMail)
        .setTo(createUserDto.getEmail())
        .setText(mailVerifyUrl);

    mailService.sendMail(mail);
    userCache.put(token, createUserDto);
  }

  public void create(String token) {
    CreateUserDto createUserDto = userCache.getIfPresent(token);
    User user = new User();
    user.setLogin(createUserDto.getLogin());
    user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
    user.setEmail(createUserDto.getEmail());
    super.create(user);
    userCache.invalidate(token);
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
