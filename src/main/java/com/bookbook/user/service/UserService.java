package com.bookbook.user.service;

import com.bookbook.general.exception.InvalidParameterException;
import com.bookbook.general.service.AbstractPersistenceService;
import com.bookbook.general.utils.UserInfo;
import com.bookbook.mail.dto.Mail;
import com.bookbook.mail.service.MailService;
import com.bookbook.template.constant.Templates;
import com.bookbook.template.service.TemplateService;
import com.bookbook.user.domain.NewUser;
import com.bookbook.user.domain.User;
import com.bookbook.user.repository.NewUserRepository;
import com.bookbook.user.repository.UserRepository;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService extends AbstractPersistenceService<User> {

  @Value("${url.frontEnd}/user/new")
  private String createUserUrl;
  @Value("${mail.from}")
  private String fromMail;
  @Value("${user.signUpExpiration}")
  private Duration signUpExpiration;

  @Autowired
  private UserRepository repository;
  @Autowired
  private NewUserRepository newUserRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private MailService mailService;
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
  public void signUp(NewUser newUser) {
    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
    newUser.setExpiration(LocalDateTime.now().plusHours(signUpExpiration.toHours()));
    NewUser saved = newUserRepository.save(newUser);

    String verifyUrl = UriComponentsBuilder.fromHttpUrl(createUserUrl)
        .queryParam("token", saved.getGuid())
        .build().toUriString();

    HashMap<String, Object> data = Maps.newHashMap();
    data.put("verifyUrl", verifyUrl);
    String html = templateService.build(Templates.USER_SING_UP, data);

    Mail mail = new Mail()
        .setFrom(fromMail)
        .setTo(newUser.getEmail())
        .setSubject("New BookBook user: " + newUser.getLogin())
        .setHtml(html);

    mailService.sendMail(mail);
  }

  @Transactional
  public String create(String newUserGuid) {
    Optional<NewUser> newUserOptional = newUserRepository.findById(newUserGuid);

    if (!newUserOptional.isPresent() || LocalDateTime.now().isAfter(newUserOptional.get().getExpiration())) {
      HashMap<String, Object> data = Maps.newHashMap();
      data.put("errorMsg", "expired");
      return templateService.build(Templates.USER_EXPIRE, data);
    }

    NewUser newUser = newUserOptional.get();
    User user = new User();
    user.setLogin(newUser.getLogin());
    user.setPassword(newUser.getPassword());
    user.setEmail(newUser.getEmail());
    super.create(user);
    newUserRepository.deleteById(newUserGuid);

    HashMap<String, Object> data = Maps.newHashMap();
    data.put("resultMsg", "User was created");
    return templateService.build(Templates.USER_CREATE, data);
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


  public Optional<User> findOneByLoginOrEmail(String loginOrEmail) {
    return repository.findOneByLoginOrEmail(loginOrEmail, loginOrEmail);
  }

  public void changePassword(String newPassword) {
    String userGuid = UserInfo.getUserGuid();
    if ("c6d06535-8fc5-47c2-8d7a-797706e1d834".equals(userGuid)) {
      throw new InvalidParameterException("You can not change admin password");
    }
    User user = repository.findById(userGuid).orElseThrow(() -> new InvalidParameterException("Incorrect user with guid: " + userGuid));
    user.setPassword(passwordEncoder.encode(newPassword));
    repository.save(user);
  }

  public void deleteAllExpiration() {
    LocalDateTime now = LocalDateTime.now();
    newUserRepository.deleteByExpirationLessThanEqual(now);
  }
}
