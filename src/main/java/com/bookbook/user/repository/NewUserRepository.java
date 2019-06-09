package com.bookbook.user.repository;

import com.bookbook.user.domain.NewUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NewUserRepository extends JpaRepository<NewUser, String> {

  void deleteByExpirationLessThanEqual(LocalDateTime now);

}
