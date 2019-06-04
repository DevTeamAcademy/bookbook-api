package com.bookbook.mail.dto;

import com.google.common.collect.Sets;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Mail {

  private String from;
  private Set<String> to;
  private String replyTo;
  private String subject;
  private String text;
  private String html;
  private List<MultipartFile> attachments = new ArrayList<>();

  public String getFrom() {
    return from;
  }

  public Mail setFrom(String from) {
    this.from = from;
    return this;
  }

  public Set<String> getTo() {
    return to;
  }

  public Mail setTo(Set<String> to) {
    this.to = to;
    return this;
  }

  public Mail setTo(String to) {
    this.to = Sets.newHashSet(to);
    return this;
  }

  public String getReplyTo() {
    return replyTo;
  }

  public Mail setReplyTo(String replyTo) {
    this.replyTo = replyTo;
    return this;
  }

  public String getSubject() {
    return subject;
  }

  public Mail setSubject(String subject) {
    this.subject = subject;
    return this;
  }

  public String getText() {
    return text;
  }

  public Mail setText(String text) {
    this.text = text;
    return this;
  }

  public String getHtml() {
    return html;
  }

  public Mail setHtml(String html) {
    this.html = html;
    return this;
  }

  public List<MultipartFile> getAttachments() {
    return attachments;
  }

  public Mail setAttachments(List<MultipartFile> attachments) {
    this.attachments = attachments;
    return this;
  }

}