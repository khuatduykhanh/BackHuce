package com.example.HuceBack.service.Impl;

import com.example.HuceBack.dto.Chat.SendMessageRequest;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.entity.Chat;
import com.example.HuceBack.entity.Message;
import com.example.HuceBack.entity.User;
import com.example.HuceBack.exception.APIException;
import com.example.HuceBack.repository.ChatRepository;
import com.example.HuceBack.repository.MessageRepository;
import com.example.HuceBack.repository.UserRepository;
import com.example.HuceBack.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceimpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Override
    public Message sendMessage(SendMessageRequest sendMessageRequest) {
        Chat chat = chatRepository.findChatById(sendMessageRequest.getChatId());
        User user = userRepository.findUserById(sendMessageRequest.getUserId());
        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(sendMessageRequest.getContent());
        message.setTimestamp(LocalDate.now());
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatMessage(Long chatId,User reqUser) {
        Chat chat = chatRepository.findChatById(chatId);
        if(! chat.getUsers().contains(reqUser)){
            throw new APIException(HttpStatus.BAD_REQUEST,"Bạn không có trong nhóm chat này");
        }
        return messageRepository.findByChatId(chat.getId());
    }

    @Override
    public Message findMessageById(Long messageId) {
        Optional<Message> opt = messageRepository.findById(messageId);
        if(opt.isPresent()){
            return opt.get();
        }
        throw  new APIException(HttpStatus.BAD_REQUEST,"Không tim thấy message id");
    }

    @Override
    public MessageBaseResponse deleteMessage(Long messageId, User reqUser) {
        Message message = findMessageById(messageId);
        if(message.getUser().getId().equals(reqUser.getId())){
            messageRepository.delete(message);
        }
        throw  new APIException(HttpStatus.BAD_REQUEST,"Bạn không thể xóa message này");
    }
}
