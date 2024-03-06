package com.example.HuceBack.service;

import com.example.HuceBack.dto.Chat.GroupChatRequest;
import com.example.HuceBack.dto.common.BaseResponse;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.entity.Chat;
import com.example.HuceBack.entity.User;
import jdk.jshell.spi.ExecutionControl;

import java.util.List;

public interface ChatService {
    BaseResponse<Chat> createChat(User reqUser, Long userId2);
    BaseResponse<Chat> findChatById(Long chatId);
    BaseResponse<List<Chat>> findAllChatByUserId(Long userId);
    BaseResponse<Chat> createGroup(GroupChatRequest req, User reqUserId);
    BaseResponse<Chat> addUserToGroup(Long userId, Long chatId, User reqUser);
    BaseResponse<Chat> renameGroup(Long chatId, String groupName, User reqUser);
    BaseResponse<Chat>removeFromGroup(Long chatId, Long userId, User reqUser);
    MessageBaseResponse deleteChat(Long chatId, Long userId);
}
