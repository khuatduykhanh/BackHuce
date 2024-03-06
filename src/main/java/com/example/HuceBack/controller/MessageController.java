package com.example.HuceBack.controller;

import com.example.HuceBack.dto.Chat.SendMessageRequest;
import com.example.HuceBack.dto.Chat.SingleChatRequest;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.entity.Message;
import com.example.HuceBack.entity.User;
import com.example.HuceBack.security.JwtProvider;
import com.example.HuceBack.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/api/messages"))
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest sendMessageRequest, HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        sendMessageRequest.setUserId(reqUser.getId());
        Message message = messageService.sendMessage(sendMessageRequest);
        return ResponseEntity.ok(message);
    }
    @PostMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatMessageHandler(@PathVariable("chatId") Long chatId, HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        List<Message> messages = messageService.getChatMessage(chatId,reqUser);
        return ResponseEntity.ok(messages);
    }
    @DeleteMapping("/{messageId}")
    public ResponseEntity<MessageBaseResponse> deleteMessageHandler(@PathVariable("messageId") Long messageId, HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        return ResponseEntity.ok(messageService.deleteMessage(messageId,reqUser));
    }
}
