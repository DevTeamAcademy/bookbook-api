package com.bookbook.user.service;

import com.bookbook.general.exception.InvalidParameterException;
import com.bookbook.general.exception.ValidationException;
import com.bookbook.mail.dto.Mail;
import com.bookbook.mail.service.MailService;
import com.bookbook.security.service.CustomAuthentication;
import com.bookbook.security.service.UserDetailsServiceImpl;
import com.bookbook.template.service.TemplateService;
import com.bookbook.user.domain.PasswordResetToken;
import com.bookbook.user.domain.User;
import com.bookbook.user.repository.ResetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PasswordService {

  @Value("${user.resetPasswordExpiration}")
  private Duration resetPasswordExpiration;
  @Value("${url.frontEnd}/password/reset")
  private String resetPasswordUrl;
  @Value("${url.frontEnd}/password/change")
  private String changePasswordUrl;
  @Value("${mail.from}")
  private String fromMail;
  @Autowired
  private MailService mailService;

  @Autowired
  private UserService userService;
  @Autowired
  private ResetPasswordTokenRepository passwordResetRepository;
  @Autowired
  private AuthorizationServerTokenServices authorizationServerTokenServices;
  @Autowired
  private TemplateService templateService;
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  public void change(String newPassword) {
    userService.changePassword(newPassword);
  }

  @Transactional
  public void forgot(String loginOrEmail) {
    User user = userService.findOneByLoginOrEmail(loginOrEmail)
        .orElseThrow(() -> new ValidationException("validation.user.password.reset", "Invalid login or email"));

    String token = UUID.randomUUID().toString();

    passwordResetRepository.deleteByUserGuid(user.getGuid());
    PasswordResetToken passwordResetToken = new PasswordResetToken();
    passwordResetToken.setToken(token);
    passwordResetToken.setUserGuid(user.getGuid());
    passwordResetToken.setExpiration(LocalDateTime.now().plusHours(resetPasswordExpiration.toHours()));
    passwordResetRepository.save(passwordResetToken);

    String url = UriComponentsBuilder.fromHttpUrl(resetPasswordUrl)
        .queryParam("token", token)
        .build().toUriString();

    Map<String, Object> model = new HashMap<>();
    model.put("resetUrl", url);
    model.put("userName", user.getLogin());
    String html = templateService.build("reset-password-email.ftl", model);

    Mail mail = new Mail()
        .setSubject("Reset password")
        .setFrom(fromMail)
        .setTo(user.getMail())
        .setHtml(html);

    mailService.sendMail(mail);
  }

  public String reset(String token) {
    PasswordResetToken reset = passwordResetRepository.findOneByToken(token)
        .filter(passwordResetToken -> LocalDateTime.now().isBefore(passwordResetToken.getExpiration()))
        .orElseThrow(() -> new InvalidParameterException("token_expired"));

    passwordResetRepository.delete(reset);

    return UriComponentsBuilder.fromHttpUrl(changePasswordUrl)
        .queryParam("access_token", generateAccessToken(reset.getUserGuid()))
        .build().toUriString();
  }

  public String generateAccessToken(String userGuid) {
    UserDetails userDetails = userDetailsService.loadUserByGuid(userGuid);
    CustomAuthentication customAuthentication = new CustomAuthentication(userDetails);

    OAuth2Request oAuth2Request = new OAuth2Request(null, "frontend", userDetails.getAuthorities(), true, new HashSet<>(Arrays.asList("read", "write")), null, null, null, null);

    OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, customAuthentication);
    return authorizationServerTokenServices.createAccessToken(oAuth2Authentication).getValue();
  }

  public void deleteAllExpiration() {
    LocalDateTime now = LocalDateTime.now();
    passwordResetRepository.deleteByExpirationLessThanEqual(now);
  }

}
