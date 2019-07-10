package com.bookbook.user.service;

import com.bookbook.user.domain.User;
import com.bookbook.user.repository.UserRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {

  @Autowired
  private UserRepository postDao;

  public User writeUser(String title, String text, String category) {
    return postDao.save(new User());
  }

}

