package com.example.HuceBack.controller;

import com.example.HuceBack.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RealtimeChat {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    public Message reciveMessage(@Payload Message message){

        return message;
    }





}
