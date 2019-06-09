package com.bookbook.user.repository;

import com.bookbook.user.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

  void deleteByExpirationLessThanEqual(LocalDateTime now);

}
