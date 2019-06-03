package com.bookbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
//@EnableAuthorizationServer
//@EnableResourceServer
@RestController
public class BookbookApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookbookApiApplication.class, args);
  }

  @RequestMapping(value = "/products")
  public String getProductName() {
    return "Honey";
  }

}
