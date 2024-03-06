package com.example.HuceBack.repository;

import com.example.HuceBack.entity.ImageData;
import com.example.HuceBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageDataRepository extends JpaRepository<ImageData,Long> {
    Optional<ImageData> findImageDataByAvatarAndUser(boolean avatar, User user);
    Optional<ImageData> findImageDataById(Long id);
    Optional<List<ImageData>> findImageDataByPostAndPostId(boolean post, Long postId);
}
