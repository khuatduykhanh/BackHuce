package com.example.HuceBack.repository;

import com.example.HuceBack.entity.PasswordResetToken;
import com.example.HuceBack.entity.User;
import com.example.HuceBack.entity.VerifyEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<PasswordResetToken,Long> {
    Optional<PasswordResetToken> findByToken(String token);

    PasswordResetToken findByUser(User user);
    boolean existsByToken(String token);
}
