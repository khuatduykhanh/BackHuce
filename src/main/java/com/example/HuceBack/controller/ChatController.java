package com.example.HuceBack.controller;

import com.example.HuceBack.dto.Chat.GroupChatRequest;
import com.example.HuceBack.dto.Chat.RenameChatRequest;
import com.example.HuceBack.dto.Chat.SingleChatRequest;
import com.example.HuceBack.entity.Chat;
import com.example.HuceBack.entity.User;
import com.example.HuceBack.security.JwtProvider;
import com.example.HuceBack.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private JwtProvider jwtProvider;
    @PostMapping("/single")
    public ResponseEntity<?> createChatHandler(@RequestBody SingleChatRequest singleChatRequest, HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        return ResponseEntity.ok(chatService.createChat(reqUser,singleChatRequest.getUserId()));
    }

    @PostMapping("/group")
    public ResponseEntity<?> createGroupHandler(@RequestBody GroupChatRequest groupChatRequest, HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        return ResponseEntity.ok(chatService.createGroup(groupChatRequest,reqUser));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<?> findChatByIdHandler(@PathVariable("chatId") Long chatId){
        return ResponseEntity.ok(chatService.findChatById(chatId));
    }

    @GetMapping("/user")
    public ResponseEntity<?> findAllChatByUserIdHandler( HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        return ResponseEntity.ok(chatService.findAllChatByUserId(reqUser.getId()));
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<?> addUserToGroupHandler(@PathVariable("chatId") Long chatId, @PathVariable("userId") Long userId, HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        return ResponseEntity.ok(chatService.addUserToGroup(userId,chatId,reqUser));
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<?> removeUserToGroupHandler(@PathVariable("chatId") Long chatId, @PathVariable("userId") Long userId, HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        return ResponseEntity.ok(chatService.removeFromGroup(userId,chatId,reqUser));
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<?> deleteChatHandler(@PathVariable("chatId") Long chatId, HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        return ResponseEntity.ok(chatService.deleteChat(chatId,reqUser.getId()));
    }

    @PutMapping("/renameGroup/{chatId}")
    public ResponseEntity<?> renameGroupHandler(@PathVariable("chatId") Long chatId, @RequestBody RenameChatRequest renameChatRequest, HttpServletRequest request){
        User reqUser = jwtProvider.getUserFromRequest(request);
        return ResponseEntity.ok(chatService.renameGroup(chatId,renameChatRequest.getGroupName(),reqUser));
    }

}
