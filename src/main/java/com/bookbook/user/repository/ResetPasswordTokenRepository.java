package com.bookbook.user.repository;

import com.bookbook.user.domain.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {

  void deleteByExpirationLessThanEqual(LocalDateTime now);

  Optional<ResetPasswordToken> findOneByToken(String token);

}

