package com.example.HuceBack.repository;

import com.example.HuceBack.entity.User;
import com.example.HuceBack.entity.VerifyEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyEmailRepository extends JpaRepository<VerifyEmail,Long> {
    Optional<VerifyEmail> findByToken(String token);
    boolean existsByToken(String token);
    VerifyEmail findByUser(User user);
}
