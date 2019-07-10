package com.bookbook.user.service;

import com.bookbook.user.domain.User;
import com.bookbook.user.repository.UserRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired
  private UserRepository postDao;

  public List<User> recentUsers(int count, int offset) {
    return postDao.findAll();
  }

}
