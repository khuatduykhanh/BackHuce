package com.example.HuceBack.repository;

import com.example.HuceBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmailAndIsEnabled(String email,boolean isEnabled);
    Optional<User> findByEmail(String email);
    User findUserById(Long id);
    boolean existsByEmail(String email);
}
