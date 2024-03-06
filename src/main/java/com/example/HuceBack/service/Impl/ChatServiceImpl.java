package com.example.HuceBack.service.Impl;

import com.example.HuceBack.dto.Chat.GroupChatRequest;
import com.example.HuceBack.dto.common.BaseResponse;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.entity.Chat;
import com.example.HuceBack.entity.User;
import com.example.HuceBack.exception.APIException;
import com.example.HuceBack.repository.ChatRepository;
import com.example.HuceBack.repository.UserRepository;
import com.example.HuceBack.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public BaseResponse<Chat> createChat(User reqUser, Long userId2) {
        User user = userRepository.findUserById(userId2);
        Chat isChatExist = chatRepository.findSingleChatByUserIds(user,reqUser);
        if(isChatExist != null){
            return new BaseResponse<>(isChatExist) ;
        }
        Chat chat = new Chat();
        chat.setCreateBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);
        Chat newChat = chatRepository.save(chat);
        return new BaseResponse<>(newChat) ;
    }

    @Override
    public BaseResponse<Chat> findChatById(Long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(chat.isPresent()){
            return new BaseResponse<>(chat.get());
        }
        throw new APIException(HttpStatus.BAD_REQUEST,"Id chua ton tai");
    }

    @Override
    public BaseResponse<List<Chat>> findAllChatByUserId(Long userId) {
        User user = userRepository.findUserById(userId);
        return new BaseResponse<>(chatRepository.findChatByUserId(user.getId()));
    }

    @Override
    public BaseResponse<Chat> createGroup(GroupChatRequest req, User reqUser) {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChat_image(req.getChat_image());
        group.setChat_name(req.getChat_name());
        group.setCreateBy(reqUser);
        group.getAdmins().add(reqUser);
        for(Long userId: req.getUserIds()){
            User user = userRepository.findUserById(userId);
            group.getUsers().add(user);
        }
        Chat newGroup = chatRepository.save(group);
        return new BaseResponse<>(newGroup);
    }

    @Override
    public BaseResponse<Chat> addUserToGroup(Long userId, Long chatId, User reqUser) {
        Optional<Chat> opt = chatRepository.findById(chatId);
        User user = userRepository.findUserById(userId);
        if(opt.isPresent()){
            Chat chat = opt.get();
            if(chat.getAdmins().contains(reqUser)){
                chat.getUsers().add(user);
                Chat newChat =  chatRepository.save(chat);
                return new BaseResponse<>(newChat);
            } else {
                throw  new APIException(HttpStatus.BAD_REQUEST,"Không phải là admin");
            }
        }
        throw  new APIException(HttpStatus.BAD_REQUEST,"Không tìm thấy chatId");
    }

    @Override
    public BaseResponse<Chat> renameGroup(Long chatId, String groupName, User reqUser) {
        Optional<Chat> opt = chatRepository.findById(chatId);
        if(opt.isPresent()){
            Chat chat = opt.get();
            if(chat.getAdmins().contains(reqUser)){
                chat.setChat_name(groupName);
                Chat newChat = chatRepository.save(chat);
                return new BaseResponse<>(newChat);
            } else {
                throw  new APIException(HttpStatus.BAD_REQUEST,"Không phải là admin");
            }
        }
        throw  new APIException(HttpStatus.BAD_REQUEST,"Không tìm thấy chatId");
    }

    @Override
    public BaseResponse<Chat> removeFromGroup(Long chatId, Long userId, User reqUser) {
        Optional<Chat> opt = chatRepository.findById(chatId);
        User user = userRepository.findUserById(userId);
        if(opt.isPresent()){
            Chat chat = opt.get();
            if(chat.getAdmins().contains(reqUser)){
                chat.getUsers().remove(user);
                Chat newChat =  chatRepository.save(chat);
                return new BaseResponse<>(newChat);
            } else if(chat.getUsers().contains(reqUser)){
                    if(user.getId().equals(reqUser.getId())){
                        chat.getUsers().remove(user);
                        Chat newChat = chatRepository.save(chat);
                        return new BaseResponse<>(newChat);
                    }
            }
            throw  new APIException(HttpStatus.BAD_REQUEST,"Không phải là admin");
        }
        throw  new APIException(HttpStatus.BAD_REQUEST,"Không tìm thấy chatId");
    }

    @Override
    public MessageBaseResponse deleteChat(Long chatId, Long userId) {
        Optional<Chat> opt = chatRepository.findById(chatId);
        if(opt.isPresent()){
            Chat chat = opt.get();
            chatRepository.delete(chat);
            return  new MessageBaseResponse(1,"Xoa thanh cong");
        }
        return  new MessageBaseResponse(0,"Xoa khong thanh cong");
    }
}
