package com.example.HuceBack.service;

import com.example.HuceBack.dto.Chat.SendMessageRequest;
import com.example.HuceBack.dto.common.BaseResponse;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.entity.Message;
import com.example.HuceBack.entity.User;

import java.util.List;

public interface MessageService {
    Message sendMessage(SendMessageRequest sendMessageRequest);
    List<Message> getChatMessage(Long chatId, User reqUser);
    Message findMessageById(Long messageId);
    MessageBaseResponse deleteMessage(Long messageId, User reqUser);
}
