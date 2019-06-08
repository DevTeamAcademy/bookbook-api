package com.bookbook.template.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class FreemarkerTemplateService implements TemplateService {

  @Autowired
  private Configuration freemarkerConfiguration;

  @Override
  public String build(String templateName, Map<String, Object> data) {
    try {
      Template template = freemarkerConfiguration.getTemplate(templateName, StandardCharsets.UTF_8.name());
      return FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
    } catch (IOException | TemplateException e) {
      throw new RuntimeException("Error while processing template.", e);
    }
  }
}