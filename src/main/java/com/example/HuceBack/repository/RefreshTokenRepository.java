package com.example.HuceBack.repository;

import com.example.HuceBack.entity.RefreshToken;
import com.example.HuceBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken findByUserInfo(User userInfo);
}
