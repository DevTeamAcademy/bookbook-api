package com.bookbook.user.repository;

import com.bookbook.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findOneByLogin(String login);

  Optional<User> findOneByLoginOrMail(String login, String email);

  boolean existsByMail(String email);

  boolean existsByLogin(String login);
}
