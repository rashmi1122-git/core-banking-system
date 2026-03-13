package com.cbs.repository;

import com.cbs.entity.RefreshToken;
import com.cbs.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String refreshToken);
}
