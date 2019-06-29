package com.bookbook.mail.service;

import com.bookbook.mail.dto.Mail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;

@Service
public class MailServiceImp implements MailService {

  @Value("${mail.maxSize}")
  private DataSize maxMailSize;
  @Autowired
  private JavaMailSender javaMailSender;

  @Override
  public void sendMail(Mail mail) {
    validateMail(mail);
    MimeMessage message = convertToMimeMessage(mail);
    checkMessageSize(message);


    javaMailSender.send(message);
  }

  private void checkMessageSize(MimeMessage message) {
    int messageSize = 0;
    try {
      messageSize = message.getSize();
    } catch (MessagingException e) {
      // TODO: 6/4/2019 LOGGER
      throw new RuntimeException(e);
    }
    if (messageSize > maxMailSize.toBytes()) {
      throw new RuntimeException("max size");
    }
  }

  private MimeMessage convertToMimeMessage(Mail mail) {
    MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));

    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(mail.getFrom());
      helper.setTo(mail.getTo().toArray(new String[]{}));
      if (Objects.nonNull(mail.getReplyTo())) {
        helper.setReplyTo(mail.getReplyTo());
      }
      if (StringUtils.isNotBlank(mail.getSubject())) {
        helper.setSubject(mail.getSubject());
      }

      if (StringUtils.isBlank(mail.getText())) {
        helper.setText(mail.getHtml(), true);
      } else if (StringUtils.isBlank(mail.getHtml())) {
        helper.setText(mail.getText());
      } else {
        helper.setText(mail.getText(), mail.getHtml());
      }

      for (MultipartFile attFile : mail.getAttachments()) {
        helper.addAttachment(Objects.requireNonNull(attFile.getOriginalFilename()), attFile);
      }
    } catch (AddressException e) {
//      throw new ValidationException("validation.email.notParsable", new String[]{e.getRef(), e.getLocalizedMessage()}, "Invalid Email Address");
    } catch (MessagingException e) {
      throw new RuntimeException("Error while sending mail", e);
    }
    return message;
  }

  private void validateMail(Mail mail) {
    if (StringUtils.isBlank(mail.getText()) && StringUtils.isBlank(mail.getHtml())) {
      throw new IllegalArgumentException("Mail must contain either text part or html part.");
    }
    if (StringUtils.isBlank(mail.getFrom())) {
      throw new IllegalArgumentException("From mail can not be empty.");
    }
    if (mail.getTo() == null || mail.getTo().isEmpty()) {
      throw new IllegalArgumentException("To mails can not be empty.");
    }
  }
}
