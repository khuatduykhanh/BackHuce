package com.example.HuceBack.repository;

import com.example.HuceBack.entity.Post;
import com.example.HuceBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByUser(User user);

}
