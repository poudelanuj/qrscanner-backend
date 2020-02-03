package com.anuj.qrscanner.repository;

import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.model.db.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUser_PhoneNumber(String phoneNumber);

    Optional<VerificationToken> findByUser(User user);

}
