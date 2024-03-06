package com.example.HuceBack.repository;

import com.example.HuceBack.entity.Chat;
import com.example.HuceBack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
    @Query("select c from Chat c join c.users u where u.id = :userId")
    List<Chat> findChatByUserId(@Param("userId") Long userId);
    @Query("select c from Chat c where c.isGroup = false And :user member of c.users And :reqUser member of c.users")
    Chat findSingleChatByUserIds(@Param("user") User user, @Param("reqUser") User reqUser);
    Chat findChatById(Long id);
}
