package com.bookbook.user.service;

import com.bookbook.general.exception.ValidationException;
import com.bookbook.general.service.AbstractPersistenceService;
import com.bookbook.mail.dto.Mail;
import com.bookbook.mail.service.MailService;
import com.bookbook.template.constant.Templates;
import com.bookbook.template.service.TemplateService;
import com.bookbook.user.domain.NewUser;
import com.bookbook.user.domain.PasswordResetToken;
import com.bookbook.user.domain.User;
import com.bookbook.user.repository.NewUserRepository;
import com.bookbook.user.repository.PasswordResetTokenRepository;
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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService extends AbstractPersistenceService<User> {

  @Value("${url.frontEnd}/user/new")
  private String createUserUrl;
  @Value("${url.frontEnd}/user/resetPassword")
  private String resetPasswordUrl;
  @Value("${mail.from}")
  private String fromMail;
  @Value("${user.resetPasswordExpiration}")
  private Duration resetPasswordExpiration;
  @Value("${user.signUpExpiration}")
  private Duration signUpExpiration;

  @Autowired
  private UserRepository repository;
  @Autowired
  private PasswordResetTokenRepository passwordResetRepository;
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

  public void forgotPassword(String loginOrEmail) {
    Optional<User> userOptional = repository.findOneByLoginOrEmail(loginOrEmail, loginOrEmail);
    if (!userOptional.isPresent()) {
      throw new ValidationException("validation.user.password.reset", "Invalid login or email");
    }

    User user = userOptional.get();
    String token = UUID.randomUUID().toString();

    PasswordResetToken passwordResetToken = new PasswordResetToken();
    passwordResetToken.setToken(token);
    passwordResetToken.setUserGuid(user.getGuid());
    passwordResetToken.setExpiration(LocalDateTime.now().plusHours(resetPasswordExpiration.toHours()));
    passwordResetRepository.save(passwordResetToken);

    String url = UriComponentsBuilder.fromHttpUrl(resetPasswordUrl)
        .queryParam("userGuid", user.getGuid())
        .queryParam("token", token)
        .build().toUriString();

    Map<String, Object> model = new HashMap<>();
    model.put("url", url);
    model.put("userName", user.getLogin());
    String html = templateService.build("reset-password-email.ftl", model);

    Mail mail = new Mail()
        .setSubject("Reset password")
        .setFrom(fromMail)
        .setTo(user.getEmail())
        .setHtml(html);

    mailService.sendMail(mail);
  }

  public void deleteAllExpiration() {
    LocalDateTime now = LocalDateTime.now();
    passwordResetRepository.deleteByExpirationLessThanEqual(now);
    newUserRepository.deleteByExpirationLessThanEqual(now);
  }
}
