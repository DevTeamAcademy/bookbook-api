package com.bookbook.template.service;

import java.util.Map;

public interface TemplateService {

  String build(String templateReference, Map<String, Object> model);

}